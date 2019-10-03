package com.example.itsability;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PrivacyNoticeDialogFragment extends DialogFragment {
    public interface HostResolveListener {
        void onPrivacyNoticeReceived();
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    NoticeDialogListener noticeDialogListener;
    HostResolveListener hostResolveListener;

    static PrivacyNoticeDialogFragment createDialog(HostResolveListener hostResolveListener) {
        PrivacyNoticeDialogFragment dialogFragment = new PrivacyNoticeDialogFragment();
        dialogFragment.hostResolveListener = hostResolveListener;
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            noticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new AssertionError("Must implement NoticeDialogListener", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        noticeDialogListener = null;
        hostResolveListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        builder
                .setTitle(R.string.share_experience_title)
                .setMessage(R.string.share_experience_message)
                .setPositiveButton(
                        R.string.agree_to_share,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Send the positive button event back to the host activity
                                noticeDialogListener.onDialogPositiveClick(PrivacyNoticeDialogFragment.this);
                                hostResolveListener.onPrivacyNoticeReceived();
                            }
                        })
                .setNegativeButton(
                        R.string.learn_more,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent browserIntent =
                                        new Intent(
                                                Intent.ACTION_VIEW, Uri.parse(getString(R.string.cloud_anchor_website)));
                                getActivity().startActivity(browserIntent);
                            }
                        });
        return builder.create();
    }
}
