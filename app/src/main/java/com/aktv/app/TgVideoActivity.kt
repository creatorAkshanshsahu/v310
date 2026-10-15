package com.aktv.app

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

/** Uses TG Video's documented embeds; the service does not publish a catalogue API. */
class TgVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dp = resources.displayMetrics.density
        fun px(n: Int) = (n * dp).toInt()
        val scroll = ScrollView(this).apply { setBackgroundColor(Accent.ABYSS); isFillViewport = true }
        val form = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(px(24), px(16), px(24), px(24))
        }
        scroll.addView(form)
        fun label(text: String, size: Float = 16f) = TextView(this).apply {
            this.text = text; textSize = size; setTextColor(Accent.TEXT)
            setPadding(0, px(8), 0, px(6)); form.addView(this)
        }
        label("TG Video", 28f)
        val types = RadioGroup(this).apply { orientation = RadioGroup.HORIZONTAL }
        val movie = RadioButton(this).apply { id = View.generateViewId(); text = "Movie"; setTextColor(Accent.TEXT) }
        val tv = RadioButton(this).apply { id = View.generateViewId(); text = "TV episode"; setTextColor(Accent.TEXT) }
        types.addView(movie); types.addView(tv); types.check(movie.id); form.addView(types)
        fun number(name: String, initial: String = ""): EditText {
            label(name)
            return EditText(this).apply {
                inputType = InputType.TYPE_CLASS_NUMBER; isSingleLine = true
                setTextColor(Accent.TEXT); setHintTextColor(Accent.DIM); hint = name
                setText(initial); minimumHeight = px(48)
                backgroundTintList = android.content.res.ColorStateList.valueOf(Accent.SIGNAL)
                form.addView(this, LinearLayout.LayoutParams(-1, -2))
            }
        }
        val id = number("TMDB ID")
        // Fields are grouped so movie mode removes irrelevant inputs.
        val season = number("Season", "1")
        val seasonLabel = form.getChildAt(form.indexOfChild(season) - 1)
        val episode = number("Episode", "1")
        val episodeLabel = form.getChildAt(form.indexOfChild(episode) - 1)
        val next = CheckBox(this).apply { text = "Autoplay next episode"; isChecked = true; setTextColor(Accent.TEXT) }
        form.addView(next)
        fun updateType() {
            val visibility = if (tv.isChecked) View.VISIBLE else View.GONE
            listOf(season, seasonLabel, episode, episodeLabel, next).forEach { it.visibility = visibility }
        }
        types.setOnCheckedChangeListener { _, _ -> updateType() }; updateType()
        label("Audio language")
        val language = Spinner(this)
        language.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
            listOf("Default", "Hindi", "English", "Japanese", "Tamil", "Telugu"))
        form.addView(language, LinearLayout.LayoutParams(-1, px(48)))
        val error = label("", 14f).apply { setTextColor(Accent.LIVE) }
        val watch = Button(this).apply { text = "▶  Watch"; minimumHeight = px(48) }
        form.addView(watch, LinearLayout.LayoutParams(-1, -2))
        watch.setOnClickListener {
            val tmdb = id.text.toString().trim().toLongOrNull()
            val s = season.text.toString().toIntOrNull(); val e = episode.text.toString().toIntOrNull()
            if (tmdb == null || tmdb <= 0) { error.text = "Enter a valid TMDB ID"; id.requestFocus(); return@setOnClickListener }
            if (tv.isChecked && (s == null || s <= 0 || e == null || e <= 0)) {
                error.text = "Season and episode must be positive numbers"; return@setOnClickListener
            }
            error.text = ""
            val path = if (tv.isChecked) "tv/$tmdb/$s/$e" else "movie/$tmdb"
            val lang = listOf("", "hi", "eng", "jap", "tam", "tel")[language.selectedItemPosition]
            val url = "https://tgvid.lovable.app/embed/$path?color=3d8bff&back=true&episodeSelector=true&autoplayNextEp=${next.isChecked}" +
                if (lang.isBlank()) "" else "&lang=$lang"
            startActivity(Intent(this, BrowserActivity::class.java).putExtra("url", url))
        }
        val back = Button(this).apply { text = "←  Back"; setOnClickListener { finish() } }
        form.addView(back)
        setContentView(scroll)
    }
}
