package org.doomer.qnotez.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import kotlinx.android.synthetic.main.fragment_backup.*

import org.doomer.qnotez.R


class BackupFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val item : MenuItem = menu!!.findItem(R.id.action_qs)
        item.setVisible(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_backup, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = getString(R.string.txt_backup_title)
    }

    companion object {
        val FRAGMENT_TAG = "BackupFragment"
    }
}