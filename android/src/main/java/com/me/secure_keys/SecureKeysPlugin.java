package com.me.secure_keys;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyInfo;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.StringCodec;

/** SecureKeysPlugin */
public class SecureKeysPlugin implements FlutterPlugin, MethodCallHandler, BasicMessageChannel.MessageHandler<String> {
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private BasicMessageChannel basicMessageChannel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "secure_keys/channel");
    channel.setMethodCallHandler(this);
    basicMessageChannel = new BasicMessageChannel(
            flutterPluginBinding.getBinaryMessenger(), "secure_keys/channel" , StringCodec.INSTANCE
    );
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("generateKeys")) {
      boolean keyGeneratedSuccess = false;
      KeyPairGenerator kpg = null;
      try {
        kpg = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (NoSuchProviderException e) {
        e.printStackTrace();
      }
      try {
        kpg.initialize(new KeyGenParameterSpec.Builder(
                "secureKey",
                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                        .setKeySize(256)
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA512).setAlgorithmParameterSpec(new ECGenParameterSpec("secp256k1"))
                        .setIsStrongBoxBacked(true)
                .build());
      } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
      }

      KeyPair kp = kpg.generateKeyPair();

      PrivateKey privateKey = kp.getPrivate();
      try {
        KeyFactory factory = KeyFactory.getInstance(privateKey.getAlgorithm(), "AndroidKeyStore");
        KeyInfo keyInfo = factory.getKeySpec(privateKey, KeyInfo.class);
        keyGeneratedSuccess = keyInfo != null;
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } catch (NoSuchProviderException e) {
        e.printStackTrace();
      } catch (InvalidKeySpecException e) {
        e.printStackTrace();
      }

      result.success(keyGeneratedSuccess);
    } else if(call.method.equals("getPublicKey")){
      byte[] publicKeyRep = null;
      try {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        PublicKey publicKey = keyStore.getCertificate("secureKey").getPublicKey();
        publicKeyRep = publicKey.getEncoded();
      } catch (KeyStoreException e) {
        e.printStackTrace();
      } catch (CertificateException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }

      result.success(publicKeyRep);
    }
  }

  @Override
  public void onMessage(String message, BasicMessageChannel.Reply<String> reply) {
  if(message != null){

  }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
