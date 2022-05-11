package it.polito.lab4.skills


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab4.R

class Skill_Adapter(
    private val skillList: MutableList<Skill>
) : RecyclerView.Adapter<SkillViewHolder>(), SkillUI.SkillSaved {

    private lateinit var skillListener: SkillUI.SkillListener

    fun setOnTodoDeleteClick(listener: SkillUI.SkillListener) {
        skillListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.skill_list_layout, parent, false)
        return SkillViewHolder(view, skillListener, this)
    }

    override fun getItemCount(): Int = skillList.size

    override fun onBindViewHolder(skillViewHolder: SkillViewHolder, position: Int) {
        skillViewHolder.bind(skillList[position])
    }

    override fun onSkillTitleUpdated(position: Int, title: String) {
        skillList[position].title = title
        skillList[position].pos = position+1
    }

    override fun onSkillDescUpdated(position: Int, description: String) {
        skillList[position].description = description
    }

}
