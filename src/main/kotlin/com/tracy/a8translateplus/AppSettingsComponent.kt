import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.UIUtil.ComponentStyle
import com.tracy.a8translateplus.net.LocalData
import com.tracy.a8translateplus.net.SourceBaidu
import com.tracy.a8translateplus.net.SourceNiu
import org.jetbrains.annotations.NotNull
import java.awt.FlowLayout
import javax.swing.ButtonGroup
import javax.swing.JPanel


/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val panel: JPanel
    private val etBaiduAppId = JBTextField()
    private val etBaiduSecret = JBTextField()
    private val rbNiu = JBRadioButton(SourceNiu)
    private val rbBaidu = JBRadioButton(SourceBaidu)
    private val radioSourceGroup = ButtonGroup()

    init {
        radioSourceGroup.add(rbNiu)
        radioSourceGroup.add(rbBaidu)
        if (LocalData.read("sourceType") == SourceBaidu) {
            rbBaidu.isSelected = true
        } else {
            rbNiu.isSelected = true
        }

        val sourcePanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply {
            add(rbNiu)
            add(rbBaidu)
        }

        panel = FormBuilder.createFormBuilder()
                .addComponent(JBLabel("翻译源 （默认小牛翻译）"))
                .addComponent(sourcePanel)
                .addSeparator()
                .addComponent(JBLabel("百度翻译配置"))
                .addComponent(JBLabel("(请到https://api.fanyi.baidu.com/doc/21免费申请开通个人开发者账号）", ComponentStyle.SMALL, UIUtil.FontColor.BRIGHTER))
                .addLabeledComponent(JBLabel("AppId "), etBaiduAppId, 1, false)
                .addLabeledComponent(JBLabel("秘钥 "), etBaiduSecret, 1, false)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }

    @get:NotNull
    var baiduAppId: String
        get() = etBaiduAppId.getText()
        set(newText) {
            etBaiduAppId.setText(newText)
        }

    @get:NotNull
    var baiduSecret: String
        get() = etBaiduSecret.getText()
        set(newText) {
            etBaiduSecret.setText(newText)
        }

    @get:NotNull
    var sourceType: String
        get() = if (rbBaidu.isSelected) SourceBaidu else SourceNiu
        set(newText) {
            if (newText == SourceBaidu) {
                rbBaidu.isSelected = true
            } else {
                rbNiu.isSelected = true
            }
        }
}