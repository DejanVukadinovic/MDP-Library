package org.example;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CertUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static X509Certificate generateV1Certificate(KeyPair keyPair, String dn, Date notBefore, Date notAfter) throws Exception {
        // Build the certificate
        X500Name issuer = new X500Name(dn);
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        X509v1CertificateBuilder certBuilder = new JcaX509v1CertificateBuilder(
                issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC").build(keyPair.getPrivate());

        return new JcaX509CertificateConverter().setProvider("BC")
                .getCertificate(certBuilder.build(contentSigner));
    }
}
