# react-native-baidu-map


在学习使用RN中，项目中需要使用定位功能，后来在网上找了一个高德地图的一个ＲＮ插件，后来在项目中怎么使用都比较困难，有的机直接使用不上，
我就直接写了一个百度地图的定位。

Example
import BaiDuLocation from './android/react-native-baidu-map';
．．．
componentDidMount() {
        console.log('AAAAAAAAAAAAAAAAA');
        this.unlisten = BaiDuLocation.addEventListener((data) =>{
            console.log(data);

            this.setState({
                cityName:data.province,

            })
            Comms.LATITUDE=data.latitude;
            Comms.LONTITUDE=data.lontitude;
            Comms.CITY=data.city;
            }

           );
        BaiDuLocation.startLocation({
            coorType: 'gcj02',
            span: 1000,
            openGps:true,
            LocationMode: 'Accuracy',
            killProcess: true,
            needAddress: true
        });
    }
