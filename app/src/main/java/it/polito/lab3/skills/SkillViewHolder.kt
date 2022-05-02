package it.polito.lab3.skills


import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.polito.lab3.R
import kotlinx.android.synthetic.main.skill_list_layout.view.*

class SkillViewHolder(
    view: View,
    skillListener: SkillUI.SkillListener,
    skillSaved: SkillUI.SkillSaved
) : RecyclerView.ViewHolder(view) {

    private val skillNumber: TextView = view.skill_number_label
    private val skillTitle: EditText = view.input_skill
    private val skillDesc: EditText = view.input_desc
    private val iconDeleteSkill: ImageView = view.icon_delete

    init {
        iconDeleteSkill.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                skillListener.onSkillDeleted(adapterPosition)
            }
        }

        skillTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString()
                if(name!= ""){
                skillSaved.run {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        skillSaved.onSkillTitleUpdated(adapterPosition, name)
                    }
                }
                }
            }
        })
        skillDesc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString()
                if(name!= ""){
                     skillSaved.run {
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            skillSaved.onSkillDescUpdated(adapterPosition, name)
                        }
                    }
                }
            }
        })

    }

    fun bind(skill: Skill) {
        skillNumber.text = String.format(
            itemView.context
                .getString(R.string.todo_number), adapterPosition + 1
        )

        skillTitle.setText(skill.title)
        skillDesc.setText(skill.description)

        iconDeleteSkill.visibility = if (adapterPosition == 0) View.GONE
        else View.VISIBLE
    }

}
