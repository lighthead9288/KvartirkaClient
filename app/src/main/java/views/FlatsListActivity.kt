package views

import adapters.FlatClickListener
import adapters.FlatsListAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kvartirkaclient.R
import com.example.kvartirkaclient.databinding.ActivityFlatsListBinding
import viewmodels.FlatsListViewModel
import viewmodelsfactories.FlatsListViewModelFactory


class FlatsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (requestPermissions())
            initView()

    }

    private fun initView() {
        val application = requireNotNull(this).application
        val binding = ActivityFlatsListBinding.inflate(layoutInflater)

        val viewModelFactory = FlatsListViewModelFactory(application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(FlatsListViewModel::class.java)
        binding.viewModel = viewModel

        binding.citySv.setIconifiedByDefault(false)
        binding.citySv.queryHint = getString(R.string.enter_city)
        binding.citySv.setOnQueryTextListener(object: SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.findCityFlats(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //  viewModel.findCityFlats(newText)
                return false
            }
        })


        binding.flatsListRv.addItemDecoration(DividerItemDecoration(application, DividerItemDecoration.VERTICAL))

        viewModel.flats.observe(this, Observer {
            val currency = it.currency
            val adapter = FlatsListAdapter(this, currency, FlatClickListener {
                val intent = Intent(this, FlatDetailsActivity::class.java).apply {
                    putExtra("flat", it)
                    putExtra("currency", currency)
                }
                startActivity(intent)

            })
            binding.flatsListRv.adapter = adapter
            adapter.submitList(it.flats)
        })

        viewModel.noFlatsFounded.observe(this, Observer {
            Toast.makeText(this, getString(R.string.np_flats_founded), Toast.LENGTH_SHORT).show()
        })

        viewModel.connectionIsUnavailable.observe(this, Observer {
            if (it)
                Toast.makeText(this, getString(R.string.network_is_unavailable), Toast.LENGTH_SHORT).show()
        })



        binding.lifecycleOwner = this

        setContentView(binding.root)
    }


    private fun requestPermissions(): Boolean {
        var isPermissionsGranted = true
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED  ){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
                isPermissionsGranted = false
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED  ){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                isPermissionsGranted = false
            }
        }
        return isPermissionsGranted


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
            if ((grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission is granted
                initView()
            }
            else {
                Toast.makeText(this, getString(R.string.location_permissions_were_not_granted), Toast.LENGTH_SHORT).show()
                initView()
            }
            return
        }

        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
                }
        }

    }


}