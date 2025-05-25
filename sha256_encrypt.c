// sha256_encrypt.c
// Encripta y desencripta un archivo usando AES-256-CBC y OpenSSL, derivando la clave con PBKDF2 y usando salt
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/evp.h>
#include <openssl/rand.h>
#include <openssl/err.h>

#define AES_BLOCK_SIZE 16
#define KEY_SIZE 32 // 256 bits
#define IV_SIZE 16  // 128 bits
#define SALT_SIZE 16
#define PBKDF2_ITER 100000

void handleErrors(const char *msg) {
    fprintf(stderr, "%s\n", msg);
    ERR_print_errors_fp(stderr);
    exit(1);
}

int encrypt_file(const char *infile, const char *outfile, const char *password) {
    FILE *fin = fopen(infile, "rb");
    FILE *fout = fopen(outfile, "wb");
    if (!fin || !fout) handleErrors("Error abriendo archivos.");

    unsigned char salt[SALT_SIZE];
    if (!RAND_bytes(salt, SALT_SIZE)) handleErrors("Error generando salt.");
    if (fwrite(salt, 1, SALT_SIZE, fout) != SALT_SIZE) handleErrors("Error escribiendo salt.");

    unsigned char key[KEY_SIZE], iv[IV_SIZE];
    if (!PKCS5_PBKDF2_HMAC(password, strlen(password), salt, SALT_SIZE, PBKDF2_ITER, EVP_sha256(), KEY_SIZE, key))
        handleErrors("Error derivando clave con PBKDF2.");
    if (!RAND_bytes(iv, IV_SIZE)) handleErrors("Error generando IV.");
    if (fwrite(iv, 1, IV_SIZE, fout) != IV_SIZE) handleErrors("Error escribiendo IV.");

    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    if (!ctx) handleErrors("Error creando contexto de cifrado.");
    if (1 != EVP_EncryptInit_ex(ctx, EVP_aes_256_cbc(), NULL, key, iv)) handleErrors("Error inicializando cifrado.");

    unsigned char inbuf[1024], outbuf[1024 + AES_BLOCK_SIZE];
    int inlen, outlen;
    while ((inlen = fread(inbuf, 1, 1024, fin)) > 0) {
        if (1 != EVP_EncryptUpdate(ctx, outbuf, &outlen, inbuf, inlen)) handleErrors("Error cifrando datos.");
        if (fwrite(outbuf, 1, outlen, fout) != (size_t)outlen) handleErrors("Error escribiendo datos cifrados.");
    }
    if (ferror(fin)) handleErrors("Error leyendo archivo de entrada.");
    if (1 != EVP_EncryptFinal_ex(ctx, outbuf, &outlen)) handleErrors("Error finalizando cifrado.");
    if (fwrite(outbuf, 1, outlen, fout) != (size_t)outlen) handleErrors("Error escribiendo datos finales cifrados.");

    EVP_CIPHER_CTX_free(ctx);
    fclose(fin);
    fclose(fout);
    printf("Archivo encriptado correctamente.\n");
    return 0;
}

int decrypt_file(const char *infile, const char *outfile, const char *password) {
    FILE *fin = fopen(infile, "rb");
    FILE *fout = fopen(outfile, "wb");
    if (!fin || !fout) handleErrors("Error abriendo archivos.");

    unsigned char salt[SALT_SIZE];
    if (fread(salt, 1, SALT_SIZE, fin) != SALT_SIZE) handleErrors("Error leyendo salt.");
    unsigned char iv[IV_SIZE];
    if (fread(iv, 1, IV_SIZE, fin) != IV_SIZE) handleErrors("Error leyendo IV.");

    unsigned char key[KEY_SIZE];
    if (!PKCS5_PBKDF2_HMAC(password, strlen(password), salt, SALT_SIZE, PBKDF2_ITER, EVP_sha256(), KEY_SIZE, key))
        handleErrors("Error derivando clave con PBKDF2.");

    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    if (!ctx) handleErrors("Error creando contexto de descifrado.");
    if (1 != EVP_DecryptInit_ex(ctx, EVP_aes_256_cbc(), NULL, key, iv)) handleErrors("Error inicializando descifrado.");

    unsigned char inbuf[1024 + AES_BLOCK_SIZE], outbuf[1024 + AES_BLOCK_SIZE];
    int inlen, outlen;
    while ((inlen = fread(inbuf, 1, 1024, fin)) > 0) {
        if (1 != EVP_DecryptUpdate(ctx, outbuf, &outlen, inbuf, inlen)) handleErrors("Error descifrando datos.");
        if (fwrite(outbuf, 1, outlen, fout) != (size_t)outlen) handleErrors("Error escribiendo datos descifrados.");
    }
    if (ferror(fin)) handleErrors("Error leyendo archivo cifrado.");
    if (1 != EVP_DecryptFinal_ex(ctx, outbuf, &outlen)) handleErrors("Error finalizando descifrado (clave o archivo incorrecto).\n");
    if (fwrite(outbuf, 1, outlen, fout) != (size_t)outlen) handleErrors("Error escribiendo datos finales descifrados.");

    EVP_CIPHER_CTX_free(ctx);
    fclose(fin);
    fclose(fout);
    printf("Archivo descifrado correctamente.\n");
    return 0;
}

int main(int argc, char *argv[]) {
    ERR_load_crypto_strings();
    OpenSSL_add_all_algorithms();
    if (argc < 5) {
        printf("Uso: %s <enc|dec> <archivo_entrada> <archivo_salida> <clave>\n", argv[0]);
        return 1;
    }
    if (strcmp(argv[1], "enc") == 0) {
        return encrypt_file(argv[2], argv[3], argv[4]);
    } else if (strcmp(argv[1], "dec") == 0) {
        return decrypt_file(argv[2], argv[3], argv[4]);
    } else {
        printf("Modo no reconocido: %s (use 'enc' o 'dec')\n", argv[1]);
        return 1;
    }
}
