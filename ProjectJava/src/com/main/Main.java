package com.main;

import CRUD.Operasi;
import CRUD.Utility;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Membuat scanner untuk input terminal
        Scanner terminalInput = new Scanner(System.in);
        String pilihanUser;
        boolean isLanjutkan = true;

        // Loop untuk terus menjalankan program sampai pengguna memutuskan untuk berhenti
        while (isLanjutkan) {
            // Membersihkan layar
            clearScreen();
            // Menampilkan menu utama
            System.out.println("Database Perpustakaan\n");
            System.out.println("1.\tLihat seluruh buku");
            System.out.println("2.\tCari data buku");
            System.out.println("3.\tTambah data buku");
            System.out.println("4.\tUbah data buku");
            System.out.println("5.\tHapus data buku");

            // Meminta input pilihan dari pengguna
            System.out.print("\n\nPilihan anda: ");
            pilihanUser = terminalInput.next();

            // Switch case berdasarkan pilihan pengguna
            switch (pilihanUser) {
                case "1":
                    // Menampilkan seluruh data buku
                    System.out.println("\n=================");
                    System.out.println("LIST SELURUH BUKU");
                    System.out.println("=================");
                    Operasi.tampilkanData();
                    break;
                case "2":
                    // Mencari data buku
                    System.out.println("\n=========");
                    System.out.println("CARI BUKU");
                    System.out.println("=========");
                    Operasi.cariData();
                    break;
                case "3":
                    // Menambah data buku baru
                    System.out.println("\n===============");
                    System.out.println("TAMBAH DATA BUKU");
                    System.out.println("===============");
                    Operasi.tambahData();
                    break;
                case "4":
                    // Mengubah data buku yang sudah ada
                    System.out.println("\n===============");
                    System.out.println("UBAH DATA BUKU");
                    System.out.println("===============");
                    Operasi.updateData();
                    break;
                case "5":
                    // Menghapus data buku
                    System.out.println("\n===============");
                    System.out.println("HAPUS DATA BUKU");
                    System.out.println("===============");
                    Operasi.deleteData();
                    break;
                default:
                    // Menangani input yang tidak valid
                    System.out.println("\nInput anda tidak ditemukan\nSilahkan pilih [1-5]");
            }

            // Menanyakan kepada pengguna apakah ingin melanjutkan atau tidak
            isLanjutkan = Utility.getYesorNo("Apakah Anda ingin melanjutkan");
        }
    }

    // Method untuk membersihkan layar
    public static void clearScreen() {
        try {
            // Jika OS adalah Windows, jalankan perintah 'cls'
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Jika bukan Windows, gunakan escape sequence untuk membersihkan layar
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {
            // Menangani kesalahan jika tidak bisa membersihkan layar
            System.err.println("Tidak bisa clear screen");
        }
    }
}
