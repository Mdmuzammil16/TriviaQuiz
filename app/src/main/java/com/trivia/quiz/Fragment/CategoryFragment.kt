package com.trivia.quiz.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trivia.quiz.Adapter.CategoryAdapter
import com.trivia.quiz.Models.CategoryModel
import com.trivia.quiz.R
import com.trivia.quiz.databinding.FragmentCategoryBinding
import com.trivia.quiz.utils.MusicClass
import com.trivia.quiz.utils.UserPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment: Fragment() {

    var _binding: FragmentCategoryBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var musicClass: MusicClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCategoryBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val list = arrayListOf<CategoryModel>(
            CategoryModel("Arts & Literacy", "" +
                    "Test Your Knowledge in Arts & Literacy", R.drawable.arts,"arts_and_literature"),
            CategoryModel("Geography", "Let's Explore more about our geographical Knowledge",
                R.drawable.geo, "geography"),
            CategoryModel("Music", "" +
                "Test Your Knowledge in Music", R.drawable.music,"music"),
            CategoryModel("Sports", "" +
                    "Test Your Knowledge in Sports", R.drawable.sports,"sport_and_leisure"),
            CategoryModel("Food", "" +
                    "Test Your Knowledge in Food", R.drawable.food,"food_and_drink"),
            CategoryModel("Flims", "" +
                    "Test Your Knowledge in Flims", R.drawable.flim,"film_and_tv")

        )


        binding.profileLayout.setOnClickListener {
            findNavController().navigate(R.id.action_global_avatorFragment)
        }


        binding.categoryRv.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRv.adapter = CategoryAdapter(list, musicClass)


    }

    override fun onStart() {
        super.onStart()

        binding.pointsTv.text = userPreference.getUserinfo("points","0")
        binding.levelTv.text = "Lvl: ${userPreference.getUserinfo("level","0")}"
        binding.profileIv.setImageResource(userPreference.getUserinfo("image").toInt())
        binding.nameTv.text = userPreference.getUserinfo("name", "")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}