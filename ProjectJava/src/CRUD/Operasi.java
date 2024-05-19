package CRUD;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operasi {

    // Fungsi untuk mengupdate data buku
    public static void updateData() throws IOException {
        File database = new File("database.txt");
        File tempDB = new File("tempDB.txt");

        try (BufferedReader bufferedInput = new BufferedReader(new FileReader(database));
             BufferedWriter bufferedOutput = new BufferedWriter(new FileWriter(tempDB))) {

            System.out.println("List Buku");
            tampilkanData(); // Menampilkan data buku yang ada

            Scanner terminalInput = new Scanner(System.in);
            System.out.print("\nMasukan nomor buku yang akan diupdate: ");
            int updateNum = terminalInput.nextInt(); // Meminta nomor buku yang akan diupdate

            String data = bufferedInput.readLine();
            int entryCounts = 0;

            while (data != null) {
                entryCounts++;
                StringTokenizer st = new StringTokenizer(data, ",");

                if (updateNum == entryCounts) {
                    // Menampilkan data buku yang akan diupdate
                    System.out.println("\nData yang ingin anda update adalah:");
                    System.out.println("---------------------------------------");
                    System.out.println("Referensi           : " + st.nextToken());
                    System.out.println("Tahun               : " + st.nextToken());
                    System.out.println("Penulis             : " + st.nextToken());
                    System.out.println("Penerbit            : " + st.nextToken());
                    System.out.println("Judul               : " + st.nextToken());

                    String[] fieldData = {"tahun", "penulis", "penerbit", "judul"};
                    String[] tempData = new String[4];
                    st = new StringTokenizer(data, ",");
                    String originalData = st.nextToken();

                    for (int i = 0; i < fieldData.length; i++) {
                        boolean isUpdate = Utility.getYesorNo("apakah anda ingin merubah " + fieldData[i]);
                        originalData = st.nextToken();
                        if (isUpdate) {
                            if (fieldData[i].equalsIgnoreCase("tahun")) {
                                System.out.print("masukan tahun terbit, format=(YYYY): ");
                                tempData[i] = Utility.ambilTahun(); // Meminta tahun terbit baru
                            } else {
                                terminalInput = new Scanner(System.in);
                                System.out.print("\nMasukan " + fieldData[i] + " baru: ");
                                tempData[i] = terminalInput.nextLine(); // Meminta data baru
                            }
                        } else {
                            tempData[i] = originalData; // Jika tidak ingin mengubah, menggunakan data asli
                        }
                    }

                    st = new StringTokenizer(data, ",");
                    st.nextToken();
                    // Menampilkan data baru yang telah diinputkan
                    System.out.println("\nData baru anda adalah ");
                    System.out.println("---------------------------------------");
                    System.out.println("Tahun               : " + st.nextToken() + " --> " + tempData[0]);
                    System.out.println("Penulis             : " + st.nextToken() + " --> " + tempData[1]);
                    System.out.println("Penerbit            : " + st.nextToken() + " --> " + tempData[2]);
                    System.out.println("Judul               : " + st.nextToken() + " --> " + tempData[3]);

                    boolean isUpdate = Utility.getYesorNo("apakah anda yakin ingin mengupdate data tersebut");

                    if (isUpdate) {
                        boolean isExist = Utility.cekBukuDiDatabase(tempData, false);

                        if (isExist) {
                            System.err.println("data buku sudah ada di database, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                            bufferedOutput.write(data); // Jika data sudah ada, membatalkan update
                        } else {
                            String tahun = tempData[0];
                            String penulis = tempData[1];
                            String penerbit = tempData[2];
                            String judul = tempData[3];

                            long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;
                            String penulisTanpaSpasi = penulis.replaceAll("\\s+", "");
                            String primaryKey = penulisTanpaSpasi + "_" + tahun + "_" + nomorEntry;

                            bufferedOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                        }
                    } else {
                        bufferedOutput.write(data); // Jika tidak ingin update, menulis data asli
                    }
                } else {
                    bufferedOutput.write(data); // Menulis data asli
                }
                bufferedOutput.newLine();
                data = bufferedInput.readLine();
            }

            bufferedOutput.flush();
        }

        // Menghapus file database asli
        if (Files.deleteIfExists(database.toPath())) {
            // Mengganti nama file temporary menjadi file database
            tempDB.renameTo(database);
        }
    }

    // Fungsi untuk menghapus data buku
    public static void deleteData() throws IOException {
        File database = new File("database.txt");
        File tempDB = new File("tempDB.txt");

        try (BufferedReader bufferedInput = new BufferedReader(new FileReader(database));
             BufferedWriter bufferedOutput = new BufferedWriter(new FileWriter(tempDB))) {

            System.out.println("List Buku");
            tampilkanData(); // Menampilkan data buku yang ada

            Scanner terminalInput = new Scanner(System.in);
            System.out.print("\nMasukan nomor buku yang akan dihapus: ");
            int deleteNum = terminalInput.nextInt(); // Meminta nomor buku yang akan dihapus

            boolean isFound = false;
            int entryCounts = 0;
            String data = bufferedInput.readLine();

            while (data != null) {
                entryCounts++;
                boolean isDelete = false;
                StringTokenizer st = new StringTokenizer(data, ",");

                if (deleteNum == entryCounts) {
                    // Menampilkan data buku yang akan dihapus
                    System.out.println("\nData yang ingin anda hapus adalah:");
                    System.out.println("-----------------------------------");
                    System.out.println("Referensi       : " + st.nextToken());
                    System.out.println("Tahun           : " + st.nextToken());
                    System.out.println("Penulis         : " + st.nextToken());
                    System.out.println("Penerbit        : " + st.nextToken());
                    System.out.println("Judul           : " + st.nextToken());

                    isDelete = Utility.getYesorNo("Apakah anda yakin akan menghapus?");
                    isFound = true;
                }

                if (!isDelete) {
                    bufferedOutput.write(data); // Jika tidak ingin menghapus, menulis data asli
                    bufferedOutput.newLine();
                }

                data = bufferedInput.readLine();
            }

            if (!isFound) {
                System.err.println("Buku tidak ditemukan");
            }

            bufferedOutput.flush();
        }

        // Menghapus file database asli
        if (Files.deleteIfExists(database.toPath())) {
            // Mengganti nama file temporary menjadi file database
            tempDB.renameTo(database);
        }
    }

    // Fungsi untuk menampilkan data buku
    public static void tampilkanData() throws IOException {
        try (BufferedReader bufferInput = new BufferedReader(new FileReader("database.txt"))) {
            System.out.println("\n| No |\tTahun |\tPenulis                |\tPenerbit               |\tJudul Buku");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            String data = bufferInput.readLine();
            int nomorData = 0;
            while (data != null) {
                nomorData++;
                StringTokenizer stringToken = new StringTokenizer(data, ",");

                stringToken.nextToken();
                System.out.printf("| %2d ", nomorData);
                System.out.printf("|\t%4s  ", stringToken.nextToken());
                System.out.printf("|\t%-20s   ", stringToken.nextToken());
                System.out.printf("|\t%-20s   ", stringToken.nextToken());
                System.out.printf("|\t%s   ", stringToken.nextToken());
                System.out.print("\n");

                data = bufferInput.readLine();
            }
            System.out.println("----------------------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            System.err.println("Database Tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahData(); // Jika database tidak ditemukan, meminta user untuk menambah data
        }
    }

    // Fungsi untuk mencari data buku
    public static void cariData() throws IOException {
        try {
            File database = new File("database.txt");
        } catch (Exception e) {
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahData(); // Jika database tidak ditemukan, meminta user untuk menambah data
            return;
        }

        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukan kata kunci untuk mencari buku: ");
        String cariString = terminalInput.nextLine();
        String[] keywords = cariString.split("\\s+");

        Utility.cekBukuDiDatabase(keywords, true); // Mencari buku berdasarkan kata kunci
    }

    // Fungsi untuk menambah data buku
    public static void tambahData() throws IOException {
        try (BufferedWriter bufferOutput = new BufferedWriter(new FileWriter("database.txt", true))) {

            Scanner terminalInput = new Scanner(System.in);
            String penulis, judul, penerbit, tahun;
            System.out.print("Masukan nama penulis: ");
            penulis = terminalInput.nextLine();
            System.out.print("Masukan judul buku: ");
            judul = terminalInput.nextLine();
            System.out.print("Masukan nama penerbit: ");
            penerbit = terminalInput.nextLine();
            System.out.print("Masukan tahun terbit, format=(YYYY): ");
            tahun = Utility.ambilTahun(); // Meminta tahun terbit

            String[] keywords = {tahun + "," + penulis + "," + penerbit + "," + judul};
            boolean isExist = Utility.cekBukuDiDatabase(keywords, false);

            if (!isExist) {
                long nomorEntry = Utility.ambilEntryPerTahun(penulis, tahun) + 1;
                String penulisTanpaSpasi = penulis.replaceAll("\\s+", "");
                String primaryKey = penulisTanpaSpasi + "_" + tahun + "_" + nomorEntry;
                // Menampilkan data yang akan ditambahkan
                System.out.println("\nData yang akan anda masukan adalah");
                System.out.println("----------------------------------------");
                System.out.println("Primary key  : " + primaryKey);
                System.out.println("Tahun terbit : " + tahun);
                System.out.println("Penulis      : " + penulis);
                System.out.println("Judul        : " + judul);
                System.out.println("Penerbit     : " + penerbit);

                boolean isTambah = Utility.getYesorNo("Apakah akan ingin menambah data tersebut");

                if (isTambah) {
                    bufferOutput.write(primaryKey + "," + tahun + "," + penulis + "," + penerbit + "," + judul);
                    bufferOutput.newLine();
                    bufferOutput.flush();
                }
            } else {
                System.out.println("Buku yang akan anda masukan sudah tersedia di dalam database dengan data berikut:");
                Utility.cekBukuDiDatabase(keywords, true); // Jika data sudah ada, menampilkan data yang ada
            }
        }
    }
}
