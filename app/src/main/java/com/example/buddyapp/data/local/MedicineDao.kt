package com.example.buddyapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicines(events: List<Medicine>)

    @Query("SELECT * FROM medicine WHERE name LIKE '%' || :query || '%'")
    suspend fun searchMedicine(query: String): List<Medicine>

}