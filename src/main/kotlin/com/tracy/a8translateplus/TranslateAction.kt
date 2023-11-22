package com.tracy.a8translateplus

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.JBColor
import com.tracy.a8translateplus.bean.TranslationBean
import com.tracy.a8translateplus.net.NetCallback
import com.tracy.a8translateplus.net.requestNetData
import java.awt.Color

/**
 * @author cuishijie
 * *
 */
class TranslateAction : AnAction() {
    companion object {
        private val icon = IconLoader.getIcon("/icons/a8.png")
    }

    private lateinit var editor: Editor
    private var latestClickTime = 0L  // 上一次的点击时间
    override fun actionPerformed(e: AnActionEvent) {
        if (!isFastClick(1000)) {
            editor = e.getData(PlatformDataKeys.EDITOR) ?: return

            // 获取选择模式对象
            val model = editor.selectionModel

            // 选中文字
            val selectedText = model.selectedText ?: return
            if (selectedText.isBlank()) return
            if (selectedText.length > 5000) {
                showPopupWindow("翻译最大字符不能超过5000")
                return
            }

            /* 第二步 ---> API查询 */
            requestNetData(selectedText, object : NetCallback<TranslationBean> {
                override fun onSuccess(data: TranslationBean) {
                    var text: String
                    if (data.tgt_text != null) {
                        if (selectedText.length < 50) {
                            text = "$selectedText:\n${data.tgt_text}"
                        } else {
                            text = "段落翻译：\n${data.tgt_text}"
                        }
                    } else {
                        text = "error:${data.error_msg}"
                    }
                    showPopupWindow(text)
                }

                override fun onFail(message: String) = showPopupWindow(message)

                override fun onError(error: String) = showPopupWindow(error)
            })
        }
    }


    /**
     * 第三步 --> 弹出对话框

     * @param result string result
     */
    private fun showPopupWindow(result: String) {
        ApplicationManager.getApplication().invokeLater {
            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(result, icon, JBColor(Color(186, 238, 186), Color(73, 117, 73)), null)
                    .setFadeoutTime(15000)
                    .setHideOnAction(true)
                    .createBalloon()
                    .show(JBPopupFactory.getInstance().guessBestPopupLocation(editor), Balloon.Position.below)
        }
    }

    /**
     * 屏蔽多次选中
     */
    private fun isFastClick(timeMillis: Long): Boolean {
        val begin = System.currentTimeMillis()
        val end = begin - latestClickTime
        if (end in 1..(timeMillis - 1)) return true
        latestClickTime = begin
        return false
    }
}