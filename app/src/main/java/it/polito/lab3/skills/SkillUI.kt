package it.polito.lab3.skills

interface SkillUI {
    interface SkillSaved {
        fun onSkillTitleUpdated(position: Int, title: String)
        fun onSkillDescUpdated(position: Int, description: String)

    }

    interface SkillListener {
        fun onSkillDeleted(position: Int)
    }
}
