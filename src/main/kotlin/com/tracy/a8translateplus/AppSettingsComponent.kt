import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.UIUtil.ComponentStyle
import com.tracy.a8translateplus.net.LocalData
import com.tracy.a8translateplus.net.SourceBaidu
import com.tracy.a8translateplus.net.SourceNiu
import org.jdesktop.swingx.JXRadioGroup
import org.jetbrains.annotations.NotNull
import javax.swing.JPanel


/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val panel: JPanel
    private val etBaiduAppId = JBTextField()
    private val etBaiduSecret = JBTextField()
    private val radioSourceGroup = JXRadioGroup<String>()

    init {
        radioSourceGroup.add(SourceNiu)
        radioSourceGroup.add(SourceBaidu)
        radioSourceGroup.selectedValue = (LocalData.read("sourceType")) ?: SourceNiu
//        radioGroup.addActionListener {
//            if (it != null) {
//                source = radioGroup.selectedValue
//            }
//        }

        panel = FormBuilder.createFormBuilder()
                .addComponent(JBLabel("翻译源 （默认小牛翻译）"))
                .addComponent(radioSourceGroup)
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
        get() = radioSourceGroup.selectedValue
        set(newText) {
            radioSourceGroup.selectedValue = newText
        }
}