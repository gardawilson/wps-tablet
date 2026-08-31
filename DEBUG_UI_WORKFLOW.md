# Workflow Pengembangan UI

Workflow ini tersedia khusus pada build variant `debug`. Build `release` tetap memakai splash dan login seperti sebelumnya.

## Penggunaan

1. Pilih build variant `debug` di Android Studio.
2. Tekan **Run**. Aplikasi `WPS Tablet UI Debug` akan terbuka tanpa splash dan login.
3. Cari dan pilih Activity yang sedang dikerjakan.
4. Aktifkan **Buka layar terakhir otomatis saat Run** agar Run berikutnya langsung menuju Activity tersebut.
5. Setelah mengubah XML, gunakan **Apply Changes** agar Activity yang terbuka diperbarui tanpa melewati alur login lagi.

Tombol **Login Normal** tetap tersedia jika layar yang diuji membutuhkan token API yang valid. Launcher hanya membuat identitas tampilan dummy ketika belum ada sesi; token dan permission API tidak dipalsukan.

## Isolasi Debug

Build debug menggunakan application ID `com.example.myapplication.debug`, sehingga dapat dipasang berdampingan dengan aplikasi release dan memiliki data aplikasi terpisah.

Beberapa Activity membutuhkan data `Intent` tertentu. Activity seperti preview PDF sengaja tidak ditampilkan karena tidak aman dibuka tanpa parameter tersebut.
