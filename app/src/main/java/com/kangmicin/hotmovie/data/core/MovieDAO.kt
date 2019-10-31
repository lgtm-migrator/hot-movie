package com.kangmicin.hotmovie.data.core

import androidx.room.*
import com.kangmicin.hotmovie.data.entity.Movie

@Dao
interface MovieDAO {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id IN (:movieIds)")
    fun loadAllByIds(movieIds: IntArray): List<Movie>

    @Query("SELECT * FROM movie WHERE isFavorite = 1")
    fun loadAllFavorite(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(vararg movies: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Update
    fun updateMovies(vararg movies: Movie)

    @Delete
    fun deleteMovies(vararg movies: Movie)
}