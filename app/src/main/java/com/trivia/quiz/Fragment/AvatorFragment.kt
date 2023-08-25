package com.trivia.quiz.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.trivia.quiz.Adapter.AvatorAdapter
import com.trivia.quiz.InterFaces.AvatorInterface
import com.trivia.quiz.R
import com.trivia.quiz.databinding.FragmentAvatorBinding
import com.trivia.quiz.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AvatorFragment : Fragment(), AvatorInterface {
    private var _binding: FragmentAvatorBinding? = null
    private val binding get() = _binding!!


    private var selectedImage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagesList = arrayListOf(
            R.drawable.redavator,
            R.drawable.person,
            R.drawable.girlavator,
            R.drawable.purpleavator,
            R.drawable.blueavator
        )
        binding.avatorRv.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.avatorRv.adapter = AvatorAdapter(imagesList, this)


        binding.saveBtn.setOnClickListener {
            val imageBundle = Bundle()
            imageBundle.putInt("image", selectedImage.toInt())
            findNavController().navigate(R.id.action_avatorFragment_to_nameFragment, imageBundle)

        }

    }


    override fun getAvatorImage(image: Int) {
        binding.currentIv.setImageResource(image)
        selectedImage = image.toString()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}