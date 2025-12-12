package com.aurora.wave.messages.call

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 通话音效管理器
 * 管理呼叫等待音和挂断音
 */
class CallSoundManager(private val context: Context) {
    
    private var toneGenerator: ToneGenerator? = null
    private var ringbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    
    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_VOICE_CALL, 80)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 开始播放呼叫等待音（回铃音）
     * 模拟电话呼叫时的"嘟...嘟..."声音
     */
    fun startRingbackTone() {
        stopRingbackTone()
        ringbackJob = scope.launch {
            while (isActive) {
                try {
                    // 播放回铃音 - 每次响1秒
                    toneGenerator?.startTone(ToneGenerator.TONE_SUP_RINGTONE, 1000)
                    delay(1000)
                    // 静音间隔3秒
                    delay(3000)
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }
            }
        }
    }
    
    /**
     * 停止播放呼叫等待音
     */
    fun stopRingbackTone() {
        ringbackJob?.cancel()
        ringbackJob = null
        toneGenerator?.stopTone()
    }
    
    /**
     * 播放挂断音（忙音/结束音）
     * 短促的"嘟"声
     */
    fun playHangupTone() {
        scope.launch {
            try {
                // 播放呼叫结束音 - 短促的嘟声
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 200)
                delay(200)
                toneGenerator?.stopTone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 播放挂断音并在完成后执行回调
     */
    fun playHangupToneAndThen(onComplete: () -> Unit) {
        scope.launch {
            try {
                // 播放挂断音
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
                delay(350)
                toneGenerator?.stopTone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            onComplete()
        }
    }
    
    /**
     * 释放资源
     */
    fun release() {
        stopRingbackTone()
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
