package CRUD;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {

    // Method untuk mendapatkan input dari pengguna dalam bentuk yes atau no
    public static boolean getYesorNo(String message) {
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n" + message + " (y/n)? ");
        String pilihanUser = terminalInput.next();

        // Loop sampai pengguna memasukkan pilihan yang benar (y/n)
        while (!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")) {
            System.out.println("Pilihan anda bukan y atau n");
            System.out.print("\n" + message + " (y/n)? ");
            pilihanUser = terminalInput.next();
        }

        return pilihanUser.equalsIgnoreCase("y");
    }

    // Method untuk membersihkan layar konsol
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception ex) {
            System.err.println("Tidak bisa clear screen");
        }
    }

    // Method untuk mengambil tahun dari input pengguna
    public static String ambilTahun() throws IOException {
        boolean tahunValid = false;
        Scanner terminalInput = new Scanner(System.in);
        String tahun = terminalInput.nextLine();

        // Loop sampai pengguna memasukkan tahun yang valid (berupa angka)
        while (!tahunValid) {
            try {
                Integer.parseInt(tahun);
                tahunValid = true;
            } catch (NumberFormatException e) {
                System.out.println("Format tahun yang anda masukan salah, format=(YYYY)");
                System.out.print("Silahkan masukan tahun terbit lagi: ");
                tahun = terminalInput.nextLine();
            }
        }
        return tahun;
    }

    // Method untuk memeriksa keberadaan buku dalam database
    public static boolean cekBukuDiDatabase(String[] keywords, boolean isDisplay) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        String data = bufferInput.readLine();
        boolean isExist = false;
        int nomorData = 0;

        // Menampilkan header tabel jika isDisplay bernilai true
        if (isDisplay) {
            System.out.println("\n| No |\tTahun |\tPenulis                |\tPenerbit               |\tJudul Buku");
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }

        // Loop untuk membaca setiap baris dalam database
        while (data != null) {
            isExist = true;

            // Memeriksa apakah setiap kata kunci terdapat dalam baris data
            for (String keyword : keywords) {
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            // Jika baris data cocok dengan kata kunci, tampilkan atau keluar dari loop
            if (isExist) {
                if (isDisplay) {
                    nomorData++;
                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("| %2d ", nomorData);
                    System.out.printf("|\t%4s  ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("|\t%-20s   ", stringToken.nextToken());
                    System.out.printf("|\t%s   ", stringToken.nextToken());
                    System.out.print("\n");
                } else {
                    break;
                }
            }

            data = bufferInput.readLine();
        }

        // Menampilkan footer tabel jika isDisplay bernilai true
        if (isDisplay) {
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }

        bufferInput.close();
        return isExist;
    }

    // Method untuk mengambil jumlah entri buku per tahun dari penulis tertentu
    public static long ambilEntryPerTahun(String penulis, String tahun) throws IOException {
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        // Loop untuk membaca setiap baris dalam database
        while (data != null) {
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");

            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            penulis = penulis.replaceAll("\\s+", "");

            // Jika penulis dan tahun cocok dengan entri dalam database, tambahkan jumlah entri
            if (penulis.equalsIgnoreCase(dataScanner.next()) && tahun.equalsIgnoreCase(dataScanner.next())) {
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        }

        bufferInput.close();
        return entry;
    }
}
