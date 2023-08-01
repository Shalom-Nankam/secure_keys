import 'package:flutter/services.dart';

class SecureKeys {
  SecureKeys._();

  static const MethodChannel _methodChannel =
      MethodChannel('secure_keys/channel');

  static const BasicMessageChannel<String> _basicMessageChannel =
      BasicMessageChannel('secure_keys/channel', StringCodec());

  Future<bool?> generateKeyPair() async {
    bool? hasGeneratedKeyPair;

    await _methodChannel.invokeMethod('generateKeys').then((value) {
      hasGeneratedKeyPair = value;
    }).catchError((error) {
      throw error;
    });
    return hasGeneratedKeyPair;
  }

//We get the public key in a byte array and use the dartsv library
//to get public key : SVPublickey.fromDER()
  Future<List<int>?> getPublicKey() async {
    List<int>? pubKey;

    await _methodChannel.invokeMethod('getPublickey').then((value) {
      pubKey = value;
    }).catchError((error) {
      throw error;
    });

    return pubKey;
  }

  Future<String?> signMessage({required String messageToSign}) async {
    String? sigHash;
    await _basicMessageChannel.send(messageToSign).catchError((error) {
      throw error;
    }).then((value) {
      sigHash = value;
    });
    return sigHash;
  }
}
