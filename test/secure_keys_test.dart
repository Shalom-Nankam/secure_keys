// import 'package:flutter_test/flutter_test.dart';
// import 'package:secure_keys/secure_keys.dart';
// import 'package:secure_keys/secure_keys_platform_interface.dart';
// import 'package:secure_keys/secure_keys_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';

// class MockSecureKeysPlatform
//     with MockPlatformInterfaceMixin
//     implements SecureKeysPlatform {

//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');
// }

// void main() {
//   final SecureKeysPlatform initialPlatform = SecureKeysPlatform.instance;

//   test('$MethodChannelSecureKeys is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelSecureKeys>());
//   });

//   test('getPlatformVersion', () async {
//     SecureKeys secureKeysPlugin = SecureKeys();
//     MockSecureKeysPlatform fakePlatform = MockSecureKeysPlatform();
//     SecureKeysPlatform.instance = fakePlatform;

//     expect(await secureKeysPlugin.getPlatformVersion(), '42');
//   });
// }
