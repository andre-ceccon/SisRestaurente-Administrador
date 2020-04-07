package br.com.grupo17.sisrestaurentecozinha.Util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import br.com.grupo17.sisrestaurentecozinha.Interfaces.Cardapio;
import br.com.grupo17.sisrestaurentecozinha.R;

public class PermissaoUtil {
    private Cardapio.CadastroPresenter cadastroPresenter;

    public PermissaoUtil(Cardapio.CadastroPresenter cadastroPresenter) {
        this.cadastroPresenter = cadastroPresenter;
    }

    private PermissionListener createPermissionListeners() {
        return new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                cadastroPresenter.abreGaleria();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                    new AlertDialog.Builder(cadastroPresenter.getContext())
                            .setTitle(R.string.dialog_title_permissao)
                            .setMessage(cadastroPresenter.getContext().getString(R.string.dialog_mensagem_permisao) + cadastroPresenter.getContext().getString(R.string.dialog_mensagem_permissao_continuacao))
                            .setPositiveButton(cadastroPresenter.getContext().getString(R.string.dialog_button_configuracao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    openSettings();
                                }
                            })
                            .setNegativeButton(cadastroPresenter.getContext().getString(R.string.dialog_button_cancelar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
                new AlertDialog.Builder(cadastroPresenter.getContext())
                        .setTitle(R.string.dialog_title_permissao)
                        .setMessage(R.string.dialog_mensagem_permisao)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                token.cancelPermissionRequest();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                token.continuePermissionRequest();
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                token.cancelPermissionRequest();
                            }
                        }).show();
            }
        };
    }

    private void openSettings() {
        cadastroPresenter.getContext()
                .startActivity(
                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", cadastroPresenter.getContext().getPackageName(), null)
                        ));
    }

    public void requestPermission() {
        Dexter.withActivity((Activity) cadastroPresenter.getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(createPermissionListeners())
                .check();
    }
}