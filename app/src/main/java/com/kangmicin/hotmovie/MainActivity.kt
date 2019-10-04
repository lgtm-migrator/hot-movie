package com.kangmicin.hotmovie

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.kangmicin.hotmovie.model.Movie
import com.kangmicin.hotmovie.model.TvShow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppActivity(), Contract.View, ListItemFragment.OnListFragmentInteractionListener {
    override fun appTitle(): String {
        return getString(R.string.app_name)
    }

    lateinit var presenter: Contract.Presenter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_movie, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }

        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initView = R.id.show_movie_menu
        val data = resources.obtainTypedArray(R.array.movies)
        val tvShowData = resources.obtainTypedArray(R.array.shows)
        val snapshot = Array<Array<String>>(data.length()) { i: Int ->
            val resourceId = data.getResourceId(i, 0)
            resources.getStringArray(resourceId)
        }

        val tvShowSnapshot =  Array<Array<String>>(data.length()) { i: Int ->
            val resourceId = tvShowData.getResourceId(i, 0)
            resources.getStringArray(resourceId)
        }

        presenter = Presenter(this, { i, type ->
            when(type) {
                Presenter.ModelType.MOVIE -> snapshot[i]
                Presenter.ModelType.TV_SHOW -> tvShowSnapshot[i]
            }
        }, 10)

        setContentView(R.layout.activity_main)

        bottom_navigation?.selectedItemId = initView
        bottom_navigation?.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.show_movie_menu -> {
                    presenter.loadMovies()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.show_tvshow_menu -> {
                    presenter.loadTvShows()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        presenter.loadMovies()
        data.recycle()
        tvShowData.recycle()
    }

    override fun displayMovies(movies: List<Movie>) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, ListItemFragment.newInstance(movies))
            .commit()
    }

    override fun displayTvShows(shows: List<TvShow>) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, ListItemFragment.newInstance(shows))
            .commit()
    }

    override fun onListFragmentInteraction(item: Parcelable?) {

        if (item is Movie) {
            val openIntent = Intent(this, MovieActivity::class.java)

            openIntent.putExtra(MovieActivity.MOVIE_KEY, item)
            startActivity(openIntent)
        }

        if(item is TvShow) {
            val openIntent = Intent(this, TvShowActivity::class.java)

            openIntent.putExtra(TvShowActivity.TV_SHOW_KEY, item)
            startActivity(openIntent)
        }

    }
}
