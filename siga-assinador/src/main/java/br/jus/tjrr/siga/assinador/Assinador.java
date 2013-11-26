package br.jus.tjrr.siga.assinador;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opensc.pkcs11.PKCS11LoadStoreParameter;
import org.opensc.pkcs11.PKCS11Provider;

public class Assinador {

    private static final String CAMINHO_LIB_LINUX = "/var/lib/opensc-pkcs11.so";

    private Provider provider;
    private KeyStore keyStore = null;

    private boolean windows;

    private Logger logger = Logger.getLogger(Assinador.class.getName());

    public Assinador() {
        windows = System.getProperty("os.name").contains("Windows");
    }

    private void carregarProvider() {
        if (provider == null && !windows) {
            try {
                this.provider = new PKCS11Provider(CAMINHO_LIB_LINUX);
                Security.addProvider(provider);
            } catch (Throwable throwable) {
                logger.log(Level.SEVERE, "Erro ao carregar provider", throwable);
            }
        }
    }

    public KeyStore carregarKeyStore(char[] password) throws NoSuchProviderException, KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        if (System.getProperty("os.name").contains("Windows")) {
            //Windows
            keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
            keyStore.load(null, null);
        } else {
            //Linux
            keyStore = KeyStore.getInstance("PKCS11", "OpenSC-PKCS11");
            PKCS11LoadStoreParameter pkcs11LoadStoreParameter = new PKCS11LoadStoreParameter();
            pkcs11LoadStoreParameter.setWaitForSlot(true);
            pkcs11LoadStoreParameter.setProtectionPIN(password);
            try {
                keyStore.load(pkcs11LoadStoreParameter);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erro ao carregar keyStore", e);
            }
        }
        return keyStore;
    }

    public List<String> listAliases() {
        ArrayList<String> listAliases = new ArrayList<String>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Iterator<String> iterator = Collections.list(keyStore.aliases()).iterator();

            while (iterator.hasNext()) {
                String alias = iterator.next();
                X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
                try {
                    x509Certificate.checkValidity();
                    listAliases.add(alias);
                } catch (CertificateNotYetValidException e) {
                    logger.log(Level.INFO, "Certificado para o alias " + alias + " não válido.");
                } catch (CertificateExpiredException e) {
                    logger.log(Level.INFO, "Certificado para o alias " + alias + " expirado.");
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao carregar lista de aliases", e);
        }

        return listAliases;
    }

    public boolean isWindows() {
        return windows;
    }

    public void setWindows(boolean windows) {
        this.windows = windows;
    }



}
