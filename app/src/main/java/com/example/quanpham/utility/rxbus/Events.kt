package com.example.quanpham.utility.rxbus

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

open class BaseEvent

class NumberTarget() : BaseEvent()
class NumberHeight(val unit: Boolean) : BaseEvent()
class EventHideFeedBack() : BaseEvent()
class ReminderUpdateTime(val time:String) : BaseEvent()
class StepLengthUpdate(val stepLength:String) : BaseEvent()
class NumberStep(val step:Int) : BaseEvent()
class NumberStepTime(val time:String) : BaseEvent()
class WeightUpdate(val weight:String) : BaseEvent()
class RepeatUpdate() : BaseEvent()
class ChangeUnit() : BaseEvent()
class StopUpdate() : BaseEvent()


class NetworkConnected(val isOn: Boolean) : BaseEvent()
class UpdateAvgValue(val typeValue : String, val avgSteps: Float, val avgCalories: Float, val avgDistance: Float, val avgHour: Float) : BaseEvent()


fun listenEvent(
    onSuccess: (e: BaseEvent) -> Unit,
    onError: (th: Throwable) -> Unit = {}
): Disposable {
    return RxBus.listen(BaseEvent::class.java)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            onSuccess(it)
        }, {
            onError(it)
        })
}

