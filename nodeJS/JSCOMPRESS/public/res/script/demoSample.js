var org_img = false;
var org_label = false;
var compressd_img = false;
var comp_label = false;




// document ready
document.addEventListener("DOMContentLoaded", function(){
    // Handler when the DOM is fully loaded
    org_img =  document.getElementById('org_img');
    compressd_img = document.getElementById('compressd_img');

    org_label = document.getElementById('org_label');
    comp_label = document.getElementById('comp_label');

    // input태그에 파일을 올린 경우 이미지 압축 처리 이벤트 리스너
    document.getElementById('file').addEventListener('change', (e) => {
        const file = e.target.files[0];
        
        // 파일이 없는 경우의 예외 처리(return)
        if (!file) return;
        console.log(file,file.size);

        org_label.innerText = "원본" + "(" + getByte(file.size) + ")";
        const _URL_org = window.URL || window.webkitURL;
        if (_URL_org) {
            org_img.src = _URL_org.createObjectURL(file);
        }

        /**
         * [options] 설정하지 않은 값들은 default 값으로 설정
         * 
         * strict: true - 압축된 이미지의 크기가 원래 이미지보다 클 때 압축된 이미지 대신 원본 이미지를 출력
         * checkOrientation: true - 이미지의 Exif Orientation 정보를 읽은 다음 이미지를 자동으로 회전 또는 플립 할 것인지 여부를 표시
         * maxWidth: Infinity
         * maxHeight: Infinity
         * minWidht: 0
         * minHeight: 0
         * width: undefined
         * height: undefined
         * quality: 0.8 - 출력 이미지의 품질. 0~1
         * mimeType: 'auto'
         * convertSize: 5000000 - PNG 파일 사이즈가 5MB 이상일 경우 JPEG로 변경. 기능 미사용시, Infinity 설정
         * beforeDraw: null
         * drew: null
         * success: null
         * error: null
         */
        const options = {
            //maxWidth: 1920,
            quality:0.3,
            success: function (result) {
                if (result.size > 5*1024*1024) { // 리사이징 했는데도 용량이 큰 경우
                    alert("파일 용량이 초과되어 업로드가 불가");
                    return;
                }
                // console.log('Output: ', result);
                console.log(new File([result], result.name, { type: result.type }));
                console.log(result.size);

                const _URL = window.URL || window.webkitURL;
                if (_URL) {
                    let resultURL = _URL.createObjectURL(result);
                    comp_label.innerText = "압축" + "(" + getByte(result.size) + ")";
                    compressd_img.src = resultURL;
                }
            },
            error: function (err) {
                console.log(err);
            }
        };


        // 압축 수행
        new Compressor(file, options);

    });

});


function getByte(bytes,decimals = 2){

    if(bytes === 0) return '0 Byte';
    
    const k = 1024;
    const dm = decimals;
    const sizes = ['Bytes','KB','MB','GB','TB','PB','EB','ZB','YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    
    return parseFloat((bytes / Math.pow(k,i))).toFixed(dm)+" "+sizes[i];

}