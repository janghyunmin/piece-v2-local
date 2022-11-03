package com.bsstandard.piece.data.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.dto.EventDTO
import com.bsstandard.piece.data.repository.EventRepository
import com.bsstandard.piece.view.adapter.event.EventAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 *packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : EventViewModel
 * author         : piecejhm
 * date           : 2022/09/11
 * description    : 이벤트 조회 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/11        piecejhm       최초 생성
 */

@HiltViewModel
class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: EventRepository = EventRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var eventAdapter = EventAdapter(this, context)
    private val eventList: ArrayList<EventDTO.Data.Event> = arrayListOf()

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    fun getEvent() {
        repo.getEvents().subscribe(
            { EventDTO ->
                LogUtil.logE("이벤트 갯수 : " + EventDTO.data.events.size)

                eventList.clear()
                for (i in ArrayList(EventDTO.data.events).indices) {
                    eventList.add(EventDTO.data.events[i])
                    eventAdapter.notifyDataSetChanged()
                }
            }, { throwable ->
                LogUtil.logE("이벤트 GET List Error!" + throwable.message)
            }
        )
    }

    fun viewInit(recyclerView: RecyclerView) {
        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = LinearLayoutManager(getApplication())
    }

    fun getEventItem(): List<EventDTO.Data.Event> {
        return eventList;
    }
}

object EventBindingAdapter {
    @BindingAdapter("app:representThumbnailPath")
    @JvmStatic
    fun loadImg(imageView: ImageView , url : String) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(20))
        Glide.with(imageView.context)
            .load(url)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}