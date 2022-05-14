package it.polito.lab4.timeSlots

interface SlotUI {
    interface SlotSaved {
        fun onSlotTitleUpdated(position: Int, title: String)
        fun onSlotDescUpdated(position: Int, description: String)


    }
    interface SlotListener {
        fun onSlotDeleted(position: Int)
        fun onSlotClick(position: Int)
    }
}