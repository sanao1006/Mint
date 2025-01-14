package me.sanao1006.core.data.util

import android.util.Patterns
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun LinkifyText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val uriHandler = LocalUriHandler.current
    val layoutResult = remember {
        mutableStateOf<TextLayoutResult?>(null)
    }
    val linksList = extractUrls(text)
    val annotatedString = buildAnnotatedString {
        append(text)
        linksList.forEach {
            addStyle(
                style = SpanStyle(
                    color = Color(0xFF1DA1F2),
                    textDecoration = TextDecoration.Underline
                ),
                start = it.start,
                end = it.end
            )
            addStringAnnotation(
                tag = "URL",
                annotation = it.url,
                start = it.start,
                end = it.end
            )
        }
    }
    Text(
        text = annotatedString,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { offsetPosition ->
                layoutResult.value?.let {
                    val position = it.getOffsetForPosition(offsetPosition)
                    annotatedString.getStringAnnotations(position, position).firstOrNull()
                        ?.let { result ->
                            if (result.tag == "URL") {
                                uriHandler.openUri(result.item)
                            }
                        }
                }
            }
        },
        fontSize = fontSize,
        fontStyle = fontStyle,
        overflow = overflow,
        onTextLayout = { layoutResult.value = it }
    )
}

private data class LinkInfo(
    val url: String,
    val start: Int,
    val end: Int
)

private fun extractUrls(text: String): List<LinkInfo> = buildList {
    val matcher = Patterns.WEB_URL.matcher(text)
    while (matcher.find()) {
        val matchStart = matcher.start(1)
        val matchEnd = matcher.end()
        val url = text.substring(matchStart, matchEnd).replaceFirst("http://", "https://")
        add(LinkInfo(url, matchStart, matchEnd))
    }
}
