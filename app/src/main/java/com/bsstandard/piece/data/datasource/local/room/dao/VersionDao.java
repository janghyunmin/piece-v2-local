package com.bsstandard.piece.data.datasource.local.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.bsstandard.piece.data.datasource.local.room.entity.Version;

import java.util.List;

/**
 * packageName    : com.bsstandard.piece.data.datasource.local.room.dao
 * fileName       : VersionDao
 * author         : piecejhm
 * date           : 2022/07/01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/07/01        piecejhm       최초 생성
 */

@Dao
public interface VersionDao {
   @Query("SELECT * FROM Version")
    LiveData<List<Version>> getAll();

   @Insert
    void insert(Version version);

   @Update
    void update(Version version);

   @Delete
    void delete(Version version);

   @Query("DELETE FROM Version")
    void deleteAll();
}
