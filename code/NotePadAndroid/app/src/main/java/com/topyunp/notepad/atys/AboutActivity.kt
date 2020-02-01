package com.topyunp.notepad.atys

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.topyunp.notepad.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.action_about)

        version_tv.text = "版本号：${this.packageManager.getPackageInfo(
            this.packageName,
            PackageManager.GET_META_DATA
        ).versionName}"

        btn_goto_yunp_top.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://yunp.top")
            startActivity(i)
        }

        btn_view_source.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://github.com/plter/NotePad")
            startActivity(i)
        }

        btn_visit_new_version.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://github.com/plter/NotePad/releases")
            startActivity(i)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}