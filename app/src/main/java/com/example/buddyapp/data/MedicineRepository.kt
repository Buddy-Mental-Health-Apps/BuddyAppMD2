package com.example.buddyapp.data

import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.api.response.DetailMedResponseItem
import com.example.buddyapp.data.api.response.MedicineResponseItem
import com.example.buddyapp.data.local.Medicine
import com.example.buddyapp.data.local.MedicineDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MedicineRepository(private val apiService: ApiService, private val medicineDao: MedicineDao) {
    suspend fun getAllMedicines(): List<MedicineResponseItem> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAllMedicines()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getMedicineDetail(): List<DetailMedResponseItem> {
        return withContext(Dispatchers.IO) {
            try {
                val anakDetails = async { apiService.getMedicineDetailAnak() }
                val antiseptikDetails = async { apiService.getMedicineDetailAntiseptik() }
                val asmaDetails = async { apiService.getMedicineDetailAsma() }
                val batukDetails = async { apiService.getMedicineDetailBatuk() }
                val betadineDetails = async { apiService.getMedicineDetailBetadine() }
                val degeneratifDetails = async { apiService.getMedicineDetailDegeneratif() }
                val demamDetails = async { apiService.getMedicineDetailDemam() }
                val diabetesDetails = async { apiService.getMedicineDetailDiabetes() }
                val diareDetails = async { apiService.getMedicineDetailDiare() }
                val dietDetails = async { apiService.getMedicineDetailDiet() }
                val hamilDetails = async { apiService.getMedicineDetailHamil() }
                val herbalDetails = async { apiService.getMedicineDetailHerbal() }
                val hidungDetails = async { apiService.getMedicineDetailHidung() }
                val hipertensiDetails = async { apiService.getMedicineDetailHipertensi() }
                val jantungDetails = async { apiService.getMedicineDetailJantung() }
                val kapasDetails = async { apiService.getMedicineDetailKapas() }
                val kecantikanDetails = async { apiService.getMedicineDetailKecantikan() }
                val kolestrolDetails = async { apiService.getMedicineDetailKolestrol() }
                val lukaDetails = async { apiService.getMedicineDetailLuka() }
                val maagDetails = async { apiService.getMedicineDetailMaag() }
                val mataDetails = async { apiService.getMedicineDetailMata() }
                val menstruasiDetails = async { apiService.getMedicineDetailMenstruasi() }
                val menyusuiDetails = async { apiService.getMedicineDetailMenyusui() }
                val mualDetails = async { apiService.getMedicineDetailMual() }
                val p3kDetails = async { apiService.getMedicineDetailP3k() }
                val peredaNyeriDetails = async { apiService.getMedicineDetailPeredaNyeri() }
                val suplemenDetails = async { apiService.getMedicineDetailSuplemen() }
                val telingaDetails = async { apiService.getMedicineDetailTelinga() }
                val tenggorokanDetails = async { apiService.getMedicineDetailTenggorokan() }
                val tulangDetails = async { apiService.getMedicineDetailTulang() }
                val vitaminDetails = async { apiService.getMedicineDetailVitamin() }

                val combinedDetails = anakDetails.await() + antiseptikDetails.await() +
                        asmaDetails.await() + batukDetails.await() +
                        betadineDetails.await() + degeneratifDetails.await() +
                        demamDetails.await() + diabetesDetails.await() +
                        diareDetails.await() + dietDetails.await() +
                        hamilDetails.await() + herbalDetails.await() +
                        hidungDetails.await() + hipertensiDetails.await() +
                        jantungDetails.await() + kapasDetails.await() +
                        kecantikanDetails.await() + kolestrolDetails.await() +
                        lukaDetails.await() + maagDetails.await() +
                        mataDetails.await() + menstruasiDetails.await() +
                        menyusuiDetails.await() + mualDetails.await() +
                        p3kDetails.await() + peredaNyeriDetails.await() +
                        suplemenDetails.await() + telingaDetails.await() +
                        tenggorokanDetails.await() + tulangDetails.await() +
                        vitaminDetails.await()

                combinedDetails
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun insertMedicines(medicines: List<Medicine>) {
        withContext(Dispatchers.IO) {
            medicineDao.insertMedicines(medicines)
        }
    }

    suspend fun searchMedicine(query: String): List<Medicine> {
        return medicineDao.searchMedicine(query)
    }
}