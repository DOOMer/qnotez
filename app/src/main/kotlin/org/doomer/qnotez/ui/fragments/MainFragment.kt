package org.doomer.qnotez.ui.fragments

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup

import dagger.android.support.AndroidSupportInjection

import com.afollestad.materialdialogs.MaterialDialog

import org.doomer.qnotez.ui.NoteDetailActivity
import org.doomer.qnotez.R
import org.doomer.qnotez.adapters.RecyclerViewAdapter
import org.doomer.qnotez.consts.NoteActions
import org.doomer.qnotez.db.NoteModel
import org.doomer.qnotez.utils.Dialogs
import org.doomer.qnotez.utils.NoteUtils
import org.doomer.qnotez.viewmodel.NoteListViewModel
import org.doomer.qnotez.viewmodel.ViewModelFactory

import java.util.ArrayList
import javax.inject.Inject

import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), OnClickListener, OnLongClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var prefs: SharedPreferences

    private var selectedItem: NoteModel? = null
    private var viewModel: NoteListViewModel? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private val itemSelectCallback = MaterialDialog.ListCallback { dialog, itemView, position, text ->
        when (position) {
            NoteActions.ACTION_VIEW -> if (selectedItem != null) {
                editItem(selectedItem!!.id)
            }
            NoteActions.ACTION_SHARE -> if (selectedItem != null) {
                NoteUtils.shareNote(selectedItem!!, activity!!)
            }
            NoteActions.ACTION_DELETE -> if (selectedItem != null) {
                val strIdTitle = R.string.msg_warning
                val strIdContent = R.string.msg_note_delete_text

                // TODO add check settings for move note to trashbox or direct delete
                val itemDelete = Dialogs.createConfirmDialog(context!!,
                        strIdTitle, strIdContent, itemDeleteCallback, null)
                itemDelete.show()
            }
        }
    }

    private val itemDeleteCallback = MaterialDialog.SingleButtonCallback { dialog, which ->
        // TODO add chek settings for move note to trashbox or direct delete
        viewModel!!.moveToTrash(selectedItem!!)
    }

    private val noteTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
    ) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = viewHolder.itemView.tag as NoteModel
            viewModel!!.moveToTrash(item)
            recyclerViewAdapter!!.notifyDataSetChanged()
            Snackbar.make(recycler_view, getString(R.string.snack_msg_move_to_trash), Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    private val noteTouchHelper = ItemTouchHelper(noteTouchCallback)

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty_text_view.text = getString(R.string.txt_empty_notes_list)
        empty_text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.text_size_for_empty_list))

        recyclerViewAdapter = RecyclerViewAdapter(ArrayList(), prefs, this, this)

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = recyclerViewAdapter
        recycler_view.setEmptyTextView(empty_text_view)

        noteTouchHelper.attachToRecyclerView(recycler_view)

        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(NoteListViewModel::class.java)
        viewModel!!.setShowTrash(false)

        // load items and make observer
        viewModel!!.getNoteItems().observe((
                activity as LifecycleOwner?)!!,
                Observer {
                    noteItems -> recyclerViewAdapter!!.addItems(noteItems!!)
                }
        )

        activity!!.title = getString(R.string.app_name)
    }

    override fun onLongClick(view: View): Boolean {
        selectedItem = view.tag as NoteModel

        val itemMenu = Dialogs.createListDialog(context!!, R.string.title_item_actions,
                R.array.item_action_names, itemSelectCallback)
        itemMenu.show()
        return true
    }

    override fun onClick(view: View) {
        val note = view.tag as NoteModel
        editItem(note.id)
    }

    private fun editItem(id: Int) {
        val di = Intent(activity, NoteDetailActivity::class.java)
        di.putExtra(NoteDetailActivity.KEY_NOTE_ID, id)
        startActivity(di)
    }

    fun quickSearch(query: String) {
        viewModel!!.quickSearch(query, false)!!.observe((activity as LifecycleOwner?)!!,
                Observer { noteItems -> recyclerViewAdapter!!.addItems(noteItems!!) })
    }

    companion object {
        val FRAGMENT_TAG = "MainFragment"
    }

}
