package com.senate_system.testplaygallery

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory


class MainActivity : AppCompatActivity() {

    var mediaSource: ExtractorMediaSource? = null
    var exoPlayer: SimpleExoPlayer? = null
    val arrayString: Array<String> = arrayOf(
        "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4",
        "https://file-examples.com/wp-content/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",
        "https://cdn1.iconfinder.com/data/icons/startup-glyph-set/64/testing-test-comparison-computer-technology-website-experiment-512.png",
        "https://file-examples.com/wp-content/uploads/2017/04/file_example_MP4_480_1_5MG.mp4",
        "https://cdn1.iconfinder.com/data/icons/startup-glyph-set/64/testing-test-comparison-computer-technology-website-experiment-512.png",
        "https://cdn1.iconfinder.com/data/icons/startup-glyph-set/64/testing-test-comparison-computer-technology-website-experiment-512.png",
        "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4"
    )

    var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bacndwidth = DefaultBandwidthMeter()
        val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bacndwidth))
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        val extractorsFactory = DefaultExtractorsFactory()

        video_view.player = exoPlayer

        exoPlayer?.playWhenReady = true
        exoPlayer?.addListener(object : Player.DefaultEventListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_BUFFERING -> {
                    }
                    Player.STATE_READY -> {
                    }
                    Player.STATE_ENDED -> {
                        play()
                    }
                }
            }
        })

        play()

    }

    fun play(){
        val path = getPath()
        if(path.contains(".mp4")){
            playVideo(path)
            showVideo()
        }else{
            countDownImage()
            playImage(path)
            showImage()
        }
    }

    fun getPath() : String {
        count++
        return if(count > arrayString.size) {
            count = 0
            arrayString[0]
        } else{
            arrayString[count-1]
        }
    }

    fun createMediaSource(uri : String) : MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer_video")).createMediaSource(Uri.parse(uri))
    }

    fun playVideo(url : String) {
        exoPlayer?.prepare(createMediaSource(url))
    }

    fun playImage(url : String) {
        Glide.with(this)
            .load(url)
            .into(imageView)
    }

    fun countDownImage() {
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("onTick")
            }

            override fun onFinish() {
                play()
                this.cancel()
            }
        }
        timer.start()
    }

    fun showVideo() {
        video_view.visibility = View.VISIBLE
        imageView.visibility = View.GONE
    }


    fun showImage() {
        video_view.visibility = View.GONE
        imageView.visibility = View.VISIBLE
    }
}
