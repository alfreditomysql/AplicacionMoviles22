package com.example.buttom2

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.buttom2.databinding.NoteItemBinding

class NotesAdapter(private var notes: MutableList<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    private val db : NotesDataBaseHelper = NotesDataBaseHelper(context)

    class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        val titleTextView: TextView =  binding.titleTextView
        val contentTextView: TextView = binding.contentTextView
        val optionButton: ImageView = binding.optionsButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {// vincula datos a una holderview
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.optionButton.setOnClickListener{
            val popupMenu = PopupMenu(holder.itemView.context, holder.optionButton)
            popupMenu.inflate(R.menu.note_item_menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.editOption -> {
                        //Editar Nota
                        val intent = Intent(holder.itemView.context , EditPostActivity::class.java).apply {
                            putExtra( "note_id", note.id)
                        }
                        holder.itemView.context.startActivity(intent)
                        true
                    }
                    R.id.deleteOption ->{
                        //Borrar Nota
                        db.deleteNote(note.id)
                        removeItem(holder.adapterPosition)
                        Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }

            }
            popupMenu.show()


            popupMenu.setOnDismissListener{
                popupMenu.dismiss()

            }
        }
    }

    fun refreshData(newNotes: List<Note>){
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    private fun removeItem(position: Int){
        notes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): Note {
        return notes[position]
    }

    fun enableSwipe(recyclerView: RecyclerView){
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerview: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = getItem(position)
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        //Actualizar nota
                        val intent = Intent(viewHolder.itemView.context, EditPostActivity::class.java).apply {
                            putExtra("note_id", note.id)
                        }
                        viewHolder.itemView.context.startActivity(intent)
                    }
                    ItemTouchHelper.RIGHT -> {
                        //Eliminar nota
                        db.deleteNote(note.id)
                        removeItem(position)
                        Toast.makeText(viewHolder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            override fun onChildDraw(
                c : Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState:Int,
                isCurrentlyActive:Boolean
            ){
                val iconLeft: Drawable? = ContextCompat.getDrawable(
                    viewHolder.itemView.context,
                    R.drawable.baseline_circle_24
                )
                val iconRight : Drawable? = ContextCompat.getDrawable(
                    viewHolder.itemView.context,
                    R.drawable.baseline_circle_24
                )

                val color = Color.parseColor("#E84876")
                val colorb = Color.parseColor("#55EDD6")
                val backgroundLeft = ColorDrawable(colorb)
                val backgroundRight = ColorDrawable(color)
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - iconLeft!!.intrinsicHeight) / 2

                if (dX > 0) { // swiping to the right (delete)
                    // Colorea el fondo del lado derecho con el color rojo
                    backgroundRight.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX.toInt(),
                        itemView.bottom
                    )
                    backgroundRight.draw(c)

                    // Colorea el borde izquierdo con el color rojo
                    backgroundRight.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + 20, // Ancho del borde que deseas colorear
                        itemView.bottom
                    )
                    backgroundRight.draw(c)

                    // Dibuja el icono de eliminar
                    val iconTop = itemView.top + (itemView.height - iconRight!!.intrinsicHeight) / 2
                    val iconBottom = iconTop + iconRight.intrinsicHeight
                    val iconLeftMargin = itemView.left + iconMargin
                    val iconRightMargin = itemView.left + iconMargin + iconRight.intrinsicWidth
                    iconRight.setBounds(iconLeftMargin, iconTop, iconRightMargin, iconBottom)
                    iconRight.draw(c)
                } else if (dX < 0) { // swiping to the left (update)
                    // Colorea el fondo del lado izquierdo con el color azul
                    backgroundLeft.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    backgroundLeft.draw(c)

                    // Colorea el borde derecho con el color azul
                    backgroundLeft.setBounds(
                        itemView.right - 20, // Ancho del borde que deseas colorear
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    backgroundLeft.draw(c)

                    // Dibuja el icono de actualizar
                    val iconTop = itemView.top + (itemView.height - iconLeft!!.intrinsicHeight) / 2
                    val iconBottom = iconTop + iconLeft.intrinsicHeight
                    val iconLeftMargin = itemView.right - iconMargin - iconLeft.intrinsicWidth
                    val iconRightMargin = itemView.right - iconMargin
                    iconLeft.setBounds(iconLeftMargin, iconTop, iconRightMargin, iconBottom)
                    iconLeft.draw(c)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            }


        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun putExtra(s: String) {
    }
}