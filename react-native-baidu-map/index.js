import {NativeModules, DeviceEventEmitter} from 'react-native';

const BaiDuLocation = NativeModules.BaiDuLocation;
const onLocationChanged = 'onReceiveLocationBaiDu';


export default class ALocation {

  static startLocation(options) {
    BaiDuLocation.startLocation(options);
  }

  static stopLocation() {
    BaiDuLocation.stopLocation();
  }

  static addEventListener(handler) {

    const listener = DeviceEventEmitter.addListener(
        onLocationChanged,
        handler,
    );
    return listener;
  }
}
