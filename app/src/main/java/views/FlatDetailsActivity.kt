package views

import adapters.FlatPhotosAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.kvartirkaclient.R
import com.example.kvartirkaclient.databinding.ActivityFlatDetailsBinding
import models.Currency
import models.Flat
import viewmodels.FlatDetailsViewModel
import viewmodelsfactories.FlatDetailsViewModelFactory

class FlatDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flat = intent.getSerializableExtra("flat") as Flat
        val currency = intent.getSerializableExtra("currency") as Currency

        val application = requireNotNull(this).application
        val binding = ActivityFlatDetailsBinding.inflate(layoutInflater)
        val viewModelFactory = FlatDetailsViewModelFactory(flat, currency)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(FlatDetailsViewModel::class.java)

        val pagerAdapter = FlatPhotosAdapter(this.applicationContext, flat.photos)
        binding.flatPhotosVp.adapter = pagerAdapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)
    }
}