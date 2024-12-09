package com.example.buddyapp.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.MainActivity
import com.example.buddyapp.data.local.BuddyRoomDatabase
import com.example.buddyapp.data.local.Quiz
import com.example.buddyapp.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private fun saveQuizResult(title: String, description: String, date: String) {
        val db = BuddyRoomDatabase.getDatabase(applicationContext).quizDao()
        val quiz = Quiz(title = title, description = description, date = date)

        // Menggunakan Coroutine untuk menulis ke database di latar belakang
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.insert(quiz)
                Log.d("Database", "Quiz saved successfully")
            } catch (e: Exception) {
                Log.e("Database", "Failed to save quiz", e)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Terima hasil prediksi dari intent
        val predictions = intent.getFloatArrayExtra("predictions") ?: FloatArray(12)
        Log.d("DEBUG", "Raw Predictions: ${predictions.joinToString()}")

        val categories = listOf(
            "Depression",
            "Anger",
            "Mania",
            "Anxiety",
            "Somatic Symptoms",
            "Suicidal Ideation",
            "Psychosis",
            "Sleep Problems",
            "Memory",
            "Repetitive Thoughts and Behaviors",
            "Dissociation",
            "Personality Functioning",
            "Substance Use"
        )

        val categoriesInIndonesian = mapOf(
            "Depression" to "Depresi",
            "Anger" to "Amarah",
            "Mania" to "Mania",
            "Anxiety" to "Kecemasan",
            "Somatic Symptoms" to "Gejala Somatik",
            "Suicidal Ideation" to "Pikiran Bunuh Diri",
            "Psychosis" to "Psikosis",
            "Sleep Problems" to "Masalah Tidur",
            "Memory" to "Memori",
            "Repetitive Thoughts and Behaviors" to "Pikiran dan Perilaku Berulang",
            "Dissociation" to "Disosiasi",
            "Personality Functioning" to "Fungsi Kepribadian",
            "Substance Use" to "Penggunaan Zat"
        )

        val descriptionInIndonesian = mapOf(
            "Depression" to "Depresi adalah gangguan suasana hati (mood) yang ditandai dengan perasaan sedih yang mendalam dan kehilangan minat terhadap hal-hal yang disukai. Seseorang dinyatakan mengalami depresi jika sudah 2 minggu merasa sedih, putus harapan, atau tidak berharga.\n" +
                    "Seseorang yang depresi umumnya menunjukkan ciri-ciri psikologi dan fisik tertentu. Ciri psikologis orang yang depresi adalah rasa cemas dan khawatir yang berlebihan, emosi yang tidak stabil, serta rasa putus asa atau frustrasi.\n" +
                    "Sementara itu, ciri-ciri fisik pada seseorang yang depresi adalah selalu merasa lelah dan tak bertenaga, pusing dan nyeri tanpa penyebab yang jelas, serta menurunnya selera makan.\n" +
                    "Sumber: https://www.alodokter.com",
            "Anger" to "Marah sendiri dapat diartikan sebagai emosi yang ditandai oleh adanya pertentangan terhadap seseorang atau perasaan setelah diperlakukan tidak benar. Kemarahan membantu kita memahami bahwa kita merasa dirugikan dan memberi dorongan untuk bertindak atau memperbaiki keadaan. Marah adalah hal yang sangat manusiawi, karena semua manusia memiliki emosi marah ini. Namun yang perlu diperhatikan adalah bagaimana cara kita untuk dapat mengontrol dan mengekspresikan emosi marah ini. Jika kita tidak dapat mengontrol emosi marah, maka dapat berdampak buruk bagi diri sendiri maupun orang lain atau lingkungan sekitar kita.\n" +
                    "\n" +
                    "Sumber : https://www.djkn.kemenkeu.go.id",
            "Mania" to "Mood euforia atau juga disebut dengan mania merupakan kondisi psikologis yang menyebabkan seseorang mengalami euforia berlebihan tanpa alasan, suasana hati yang sangat intens, hiperaktif dan delusional. Mania adalah salah satu gejala yang umum dari gangguan bipolar.\n" +
                    "\n" +
                    "Mania dapat berubah menjadi berbahaya karena beberapa penyebab. Seseorang mungkin tidak tidur atau makan ketika berada pada keadaan maniak. Mereka mungkin berkaitan dengan perilaku beresiko dan membahayakan mereka sendiri. Orang orang dengan mania memiliki resiko lebih tinggi mengalami halusinasi dan gangguan persepsi lain.\n" +
                    "\n" +
                    "Penderita dengan mania menunjukkan rasa senang dan euforia yang ekstrim seperti suasana hati intens lain. Mereka nampak hiperaktif dan mungkin mengalami halusinasi atau delusi. Beberapa pasien merasa gelisah dan sangat cemas. Suasana hati seseorang dengan maniak dapat berubah dengan cepat dari maniak ke depresif, dengan tingkat energy yang sangat rendah\n" +
                    "\n" +
                    "Sumber : https://www.honestdocs.id",
            "Anxiety" to "Kecemasan adalah suatu istilah yang menggambarkan gangguan psikologis yang dapat memiliki karakteristik yaitu berupa rasa takut, keprihatinan terhadap masa depan, kekhawatiran yang berkepanjangan, dan rasa gugup. Rasa cemas memang biasa dihadapi semua orang. Namun, rasa cemas disebut gangguan psikologis ketika rasa cemas menghalangi seseorang untuk menjalani kehidupan sehari-hari dan menjalani kegiatan produktif.\n" +
                    "\n" +
                    "Rasa cemas disebabkan oleh kombinasi faktor-faktor tertentu. Seperti gangguan mental lainnya, rasa cemas disebabkan oleh gagalnya saraf-saraf otak untuk mengontrol emosi dan rasa takut. Contohnya stress dapat mengubah alur komunikasi sel-sel saraf dalam sirkuit otak. Hal ini akan mengubah struktur otak tertentu yang mengkontrol emosi. Struktur otak tertentu ini pada awalnya dibentuk dari genetik dan keturunan keluarga.\n" +
                    "\n" +
                    "Sumber : https://www.docdoc.com",
            "Somatic Symptoms" to "Gangguan somatisasi atau somatic symptom disorder (SSD) merupakan suatu kondisi medis berupa sekumpulan gejala fisik yang muncul akibat masalah psikologis yang dialami penderitanya. Kondisi ini kerap menyebabkan penderitanya merasa cemas berlebihan dengan kondisi tubuhnya. \n" +
                    "\n" +
                    "Gangguan ini muncul akibat kewaspadaan berlebihan terhadap sensasi-sensasi fisik di tubuh yang salah diinterpretasikan penderitanya sebagai sebuah penyakit. Sejumlah faktor lain juga diduga dapat meningkatkan risiko seseorang mengalami SSD yaitu faktor genetik, mengalami kejadian traumatis, seperti menjadi korban pelecehan seksual atau kekerasan fisik, riwayat keluarga yang sering menderita penyakit, stres atau depresi, penyalahgunaan NAPZA, memiliki riwayat gangguan cemas.\n" +
                    "\n" +
                    "Sumber : https://www.siloamhospitals.com",
            "Suicidal Ideation" to "Pikiran bunuh diri atau suicidal thought adalah pikiran atau perasaan yang mengungkapkan keinginan untuk mengakhiri hidup. Biasanya, mereka yang mengalaminya, memiliki masalah yang cukup serius. Mereka beranggapan, bunuh diri adalah cara terbaik untuk menghilangkan beban tersebut. \n" +
                    "\n" +
                    "Pikiran untuk bunuh diri menjadi masalah yang sangat serius dan harus segera tertangani dengan tepat. Jika seseorang memiliki pikiran untuk bunuh diri, maka sebaiknya segera mencari pertolongan pertama. Pertolongan pertama ini penting agar dapat mencegah tindakan bunuh diri yang berbahaya.\n" +
                    "\n" +
                    "Pikiran untuk bunuh diri biasa terjadi pada orang yang menderita stres dan masalah kesehatan mental. Misalnya trauma, depresi atau gangguan bipolar. Kondisi ini memang tidak boleh membiarkannya terus terjadi dan sebaiknya segera mencari pertolongan.\n" +
                    "\n" +
                    "Sumber : https://www.halodoc.com",
            "Psychosis" to "Psikosis merupakan suatu gejala yang timbul karena adanya otak mengalami gangguan saat memroses informasi. Perubahan ini menyebabkan seseorang kehilangan daya nilai realita sehingga ia mampu berpikir, mendengar, melihat, atau merasakan sesuatu yang tidak nyata. \n" +
                    "\n" +
                    "Pengalaman psikosis ini dapat dialami sejak usia remaja atau dewasa muda. Sampai saat ini belum ditemukan adanya penyebab pasti dari kondisi ini. Beberapa faktor risiko yang berpotensi menimbulkan psikosis adalah genetik, zat psikoaktif, trauma psikologis, serta cedera atau penyakit fisik. Psikosis juga dapat ditemukan pada beberapa gangguan mental, seperti skizofrenia atau gangguan afektif bipolar.\n" +
                    "\n" +
                    "Sumber : https://herminahospitals.com",
            "Sleep Problems" to "Gangguan tidur adalah kelainan pada pola tidur seseorang. Kondisi ini dapat menimbulkan penurunan kualitas tidur yang berdampak pada kesehatan dan keselamatan penderitanya.\n" +
                    "\n" +
                    "Gangguan tidur dapat ditandai dengan mengantuk di siang hari, sulit tidur di malam hari, atau siklus tidur dan bangun tidur yang tidak teratur. Gangguan tidur yang tidak ditangani dengan baik dapat meningkatkan risiko munculnya berbagai penyakit lain, seperti hipertensi dan penyakit jantung.\n" +
                    "\n" +
                    "Sumber : https://www.alodokter.com\n",
            "Memory" to "Gangguan memori terjadi karena ada gangguan pada otak. Kondisi ini menyebabkan pengidapnya mengalami kesulitan untuk menyimpang, mengendalikan, dan mengingat kembali memori. Padahal, sebelumya memori tersebut sudah diingat atau dialami sendiri. Kondisi ini tidak boleh disepelekan karena bisa memburuk seiring berjalannya waktu. \n" +
                    "\n" +
                    "Jika tidak ditangani dengan cepat dan tepat, gangguan ini bisa memicu terjadinya penyakit Alzheimer. Seperti diketahui, salah satu gejala khas dari penyakit ini adalah hilangnya ingatan atau susah untuk mengingat sesuatu.\n" +
                    "\n" +
                    "Cukup banyak faktor risiko yang dapat menyebabkan seseorang alami gangguan memori, antara lain trauma otak (misalnya, operasi, dan cedera kepala), Stroke, penggunaan alkohol yang berlebihan, faktor risiko untuk gangguan kognitif ringan, ini termasuk tekanan darah tinggi (hipertensi), dan diabetes, orang dengan tingkat pendidikan, olahraga fisik dan mental, dan sosialisasi yang rendah, orang dengan mutasi gen APOE juga memiliki risiko tinggi untuk terkena gangguan terkait memori.\n" +
                    "\n" +
                    "Sumber : https://www.halodoc.com",
            "Repetitive Thoughts and Behaviors" to "Perilaku berulang-ulang disertai pemikiran terus menerus pada satu masalah, misalnya terkait dengan kebersihan, ini bisa jadi adalah gejalaobsessive-compulsive disorder (OCD). OCD adalah gangguan mental yang menyebabkan penderitanya merasa harus terus menerus mengulangi tindakan tertentu. Bila tidak dilakukan, penderitanya akan berasa cemas atau takut, sehingga tidak dapat menghindari keinginan mengulangi tindakan itu. Obsessive-compulsive personality disorder (OCPD) dan OCD memiliki kemiripan berupa adanya pemikiran posesif yang sulit dikendalikan dan perilaku kompulsif. Pada penderita OCPD, penderita mempercayai bahwa pemikiran dan perilakunya adalah yang paling tepat, sedangkan penderita OCD menyadari bahwa perilakunya tidak benar.\n" +
                    "\n" +
                    "Sumber : https://www.alodokter.com",
            "Dissociation" to "Disosiasi adalah periode istirahat dalam cara pikiran Anda menangani informasi. Orang yang mengalami disosiasi mungkin merasa terputus dari pikiran, perasaan, ingatan, dan lingkungan sekitarnya. Ini dapat mempengaruhi rasa identitas dan persepsi orang tersebut tentang waktu.\n" +
                    "\n" +
                    "Gejalanya sering hilang dengan sendirinya. Mungkin butuh berjam-jam, berhari-hari, atau berminggu-minggu. Orang yang mengalami hal tersebut memerlukan perawatan, disosiasi dapat terjadi sebagai akibat dari seseorang memiliki pengalaman yang sangat mengganggu atau jika mereka memiliki gangguan kesehatan mental seperti skizofrenia.\n" +
                    "\n" +
                    "Ketika seseorang mengalami disosiasi, orang tersebut mungkin melupakan hal-hal atau memiliki celah dalam ingatannya. Orang yang mengalami disosiasi mungkin berpikir dunia fisiknya tidak nyata atau bahkan dirinya sendiri tidak nyata.\n" +
                    "\n" +
                    "Sumber : https://soa-edu.com",
            "Personality Functioning" to "Gangguan kepribadian adalah keadaan saat seseorang menunjukkan perilaku dan cara berpikir yang tidak sehat. Kondisi dengan istilah medis personality disorder ini akan menunjukkan sikap dan tindakan yang berbeda dari apa yang masyarakat anggap sebagai hal yang normal. \n" +
                    "\n" +
                    "Hal ini akan membuat pengidapnya mengalami keterbatasan yang cukup signifikan dalam pekerjaan, sosialisasi dengan orang lain, hingga hubungan dengan lawan jenis. Selain itu, pengidap juga biasanya memiliki keterampilan dalam menyelesaikan masalah yang buruk.\n" +
                    "\n" +
                    "Sumber : https://www.halodoc.com",
            "Substance Use" to "Gangguan penggunaan zat dapat mempengaruhi orang-orang dari semua lapisan masyarakat dan semua kelompok umur. Mereka mempengaruhi baik mereka yang memiliki masalah dengan penggunaan narkoba dan teman-teman, keluarga, rekan kerja, dan rekan-rekan dalam hidup mereka. Ketika kita menggunakan istilah gangguan penggunaan zat , kita berbicara tentang istilah resmi untuk apa yang secara umum dikenal di masyarakat sebagai “kecanduan”. Tidak semua orang yang menggunakan zat memiliki gangguan penggunaan zat. Gejala gangguan penggunaan zat dapat menyebabkan masalah dalam perilaku, hubungan, dan respons emosional.\n" +
                    "\n" +
                    "Penggunaan zat mencakup berbagai zat, dan beberapa di antaranya sangat berbeda satu sama lain. Itulah mengapa tanda dan gejala fisik penggunaan zat akan berubah tergantung zatnya. Namun, penggunaan zat memiliki gejala perilaku yang umum. Beberapa tanda dan gejala dari semua gangguan penggunaan zat meliputi:\n" +
                    "\n" +
                    "Memiliki dorongan kuat untuk menggunakan zat\n" +
                    "Membutuhkan lebih banyak zat untuk mendapatkan efek yang sama (toleransi)\n" +
                    "Menghabiskan banyak waktu untuk memikirkan substansi (bagaimana rasanya, di mana / kapan / bagaimana mendapatkan lebih banyak, dll.)\n" +
                    "Kesulitan menghentikan penggunaan, meskipun orang tersebut menginginkannya\n" +
                    "Perubahan dalam tidur atau nafsu makan (terlalu banyak atau terlalu sedikit)\n" +
                    "Memiliki reaksi fisik negatif (putus zat) jika berhenti menggunakan zat tersebut (perasaan gemetar, pusing, depresi, keringat berlebih, sakit kepala, sakit perut, dll.)\n" +
                    "\n" +
                    "Sumber : https://mentalhealthtx.org\n"
        )

        // Interpretasikan hasil
        val resultText = StringBuilder()

        // Tampilkan hasil
        resultText.append("Hasil: ")

// Temukan indeks dengan score tertinggi
        val highestScoreIndex = predictions.indices.maxByOrNull { predictions[it] } ?: -1

// Jika ada score tertinggi, tampilkan kategori dengan akurasi tertinggi
        if (highestScoreIndex != -1) {
            val englishCategory = categories[highestScoreIndex]
            val indonesianCategory = categoriesInIndonesian[englishCategory] ?: englishCategory
            val description = descriptionInIndonesian[englishCategory] ?: "Deskripsi tidak tersedia"

            val maxPrediction = predictions.maxOrNull() ?: 1f
            val minPrediction = predictions.minOrNull() ?: 0f

            // Pastikan kita menghindari pembagian dengan nol
            val normalizedPredictions = predictions.map {
                if (maxPrediction != minPrediction) {
                    (it - minPrediction) / (maxPrediction - minPrediction)
                } else {
                    0f
                }
            }

            // Ambil skor yang sudah dinormalisasi untuk kategori teratas
            val normalizedScore = normalizedPredictions[highestScoreIndex]

            // Debug untuk melihat skor yang dinormalisasi
            println("Highest Category: $indonesianCategory, Normalized Score: $normalizedScore")

            resultText.append("$indonesianCategory- ${if (normalizedScore > 0.5f) "Tinggi" else "Rendah"}\n")

            binding.resultTitle.text = resultText.toString()

            binding.resultDescription.text = description

            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy - HH:mm")
            val formattedDate = currentTime.format(formatter)

            binding.date.text = formattedDate
            saveQuizResult(indonesianCategory, description, formattedDate)

            val intent = Intent(this, QuizDetailActivity::class.java).apply {
                putExtra("title", indonesianCategory)
                putExtra("description", description)
                putExtra("date", formattedDate)
            }
            startActivity(intent)
            finish()

        }


        // Toolbar back button
        binding.toolbarResult.setNavigationOnClickListener {
            finish()
        }

        // Tombol "Cari Obat"
        binding.findMedicineButton.setOnClickListener {
            // Aksi untuk tombol cari obat
        }

        // Tombol "Kembali"
        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
