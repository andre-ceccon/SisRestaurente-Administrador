package br.com.grupo17.sisrestaurentecozinha.Interfaces;

public interface InterfaceBase {
    interface ProgressVisibility {
        void setProgressVisibility(boolean visibility);
    }

    interface ButtonEnabled extends InterfaceBase.ProgressVisibility{
        void setButtonEnabled(boolean enabled);
    }
}