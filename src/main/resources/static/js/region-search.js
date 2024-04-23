let position = new naver.maps.LatLng(37.3595704, 127.105399)
let mapOptions = {
    center: position,
    zoom: 6,
    zoomControl: true,
    zoomControlOptions: {
        position: naver.maps.Position.TOP_RIGHT
    }
};
let map = new naver.maps.Map('map', mapOptions);

// 날씨 검색 버튼 생성
function createWeatherButton(point) {
    const button = document.createElement('button');
    button.textContent = '날씨 조회';

    button.addEventListener('click', function () {
        // 좌표 정보 가져오기
        const lat = point.lat;
        const lng = point.lng;

        // 날씨 정보 조회 함수 호출
        getWeatherByCoordinates(lat, lng);
    });

    return button;
}

// 좌표를 이용하여 날씨 정보를 조회하는 함수
function getWeatherByCoordinates(lat, lng, address) {
    // 지역명 표시
    document.getElementById('location').textContent = `${address}`;

    // 격자 좌표 변환 요청
    fetch(`http://localhost:8080/api/v1/weather/convert-grid?lat=${lat}&lng=${lng}`)
        .then(response => response.json())
        .then(data => {
            const nx = data.nx;
            const ny = data.ny;

            // 날씨 정보 조회 요청
            fetch(`http://localhost:8080/api/v1/weather/by-current?nx=${nx}&ny=${ny}`)
                .then(response => response.json())
                .then(data => {
                    // 기준 날짜와 시간 정보 추출
                    const baseDate = data.response.body.items.item[0].baseDate;
                    const baseTime = data.response.body.items.item[0].baseTime;

                    // 기준 시간을 시, 분으로 분할
                    const baseHour = parseInt(baseTime.substring(0, 2));
                    const ampm = baseHour >= 12 ? '오후' : '오전';
                    const displayHour = baseHour % 12 || 12;

                    // 기준 날짜를 형식에 맞게 변환 (YYYY. MM. DD)
                    const formattedDate = `${baseDate.slice(0, 4)}. ${baseDate.slice(4, 6)}. ${baseDate.slice(6, 8)}`;

                    // 결과를 HTML에 적용
                    document.getElementById('base-date').textContent = `${formattedDate} ${ampm} ${displayHour}시 기준`;

                    const weatherList = document.getElementById('weather-list');
                    weatherList.innerHTML = ''; // 이전에 표시된 날씨 정보 지우기

                    data.response.body.items.item.forEach(item => {
                        const listItem = document.createElement('li');
                        const category = replaceCategory(item.category);
                        const value = replaceCategoryValue(item.category, item.obsrValue); // 각 카테고리에 대한 값 대체
                        listItem.textContent = `${category}: ${value}`;
                        weatherList.appendChild(listItem);
                    });

                    // 향후 6시간 동안의 날씨 예보 조회 요청
                    fetch(`http://localhost:8080/api/v1/weather/by-hour?nx=${nx}&ny=${ny}`)
                        .then(response => response.json())
                        .then(data => {
                            const weatherList = document.getElementById('six-hour-weather-list');
                            weatherList.innerHTML = ''; // 이전에 표시된 날씨 정보 지우기

                            // 날씨 정보를 화면에 동적으로 표시하기
                            data.forEach(item => {
                                const listItem = document.createElement('li');
                                const fcstTime = item.fcstTime;
                                const forecastValues = item.forecastValues;

                                // 시간대 별로 묶어서 화면에 표시하기
                                let htmlContent = `<h5>${fcstTime.slice(0, 2)}:${fcstTime.slice(2)}</h5>`;
                                htmlContent += '<ul>';

                                // forecastValues에 대한 정보 표시
                                for (const [key, value] of Object.entries(forecastValues)) {
                                    const replacedCategory = replaceCategory(key); // 카테고리 한글로 대체
                                    const replacedValue = replaceCategoryValue(key, value); // 값 대체
                                    htmlContent += `<li>${replacedCategory}: ${replacedValue}</li>`;
                                }

                                htmlContent += '</ul>';
                                listItem.innerHTML = htmlContent;

                                weatherList.appendChild(listItem);
                            });
                        })
                        .catch(error => console.error('날씨 정보를 가져오는 중 오류 발생:', error));
                })
                .catch(error => console.error('날씨 정보를 가져오는 중 오류 발생:', error));
        })
        .catch(error => console.error('격자 좌표를 가져오는 중 오류 발생:', error));
}


// markers 배열 초기화
let markers = [];

document.getElementById('search-weather').addEventListener('click', function () {
    // 이전에 추가된 마커들 제거
    removeMarkers();

    // 입력된 주소 가져오기
    const query = document.getElementById('search-location').value;

    // 주소를 이용하여 좌표 정보 가져오기
    fetch(`http://localhost:8080/api/v1/weather/geocode?query=${query}`)
        .then(response => response.json())
        .then(data => {
            const searchResult = document.getElementById('search-result');
            searchResult.innerHTML = ''; // 이전 검색 결과 지우기

            // 검색 결과를 화면에 표시하고 각 결과에 날씨 조회 버튼 생성하기
            data.forEach(point => {
                const listItem = document.createElement('a');
                listItem.href = '#'; // 링크 설정
                listItem.classList.add('list-group-item', 'list-group-item-action'); // 부트스트랩 클래스 추가
                listItem.textContent = `${point.roadAddress}`;


                // 클릭 이벤트 처리
                listItem.addEventListener('click', function () {
                    map.setCenter(marker.getPosition());
                    // 지도를 약간의 줌인
                    map.setZoom(12);

                    // 좌표 정보 가져오기
                    const lat = point.lat;
                    const lng = point.lng;

                    // 날씨 조회 함수 호출
                    getWeatherByCoordinates(lat, lng, point.roadAddress);
                });

                searchResult.appendChild(listItem);

                // 마커 추가하기
                const marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(point.lat, point.lng),
                    map: map
                });

                // 마커 클릭 이벤트 처리
                naver.maps.Event.addListener(marker, 'click', function () {
                    // 좌표 정보 가져오기
                    const lat = marker.getPosition().lat();
                    const lng = marker.getPosition().lng();

                    getWeatherByCoordinates(point.lat, point.lng, point.roadAddress);
                });

                // markers 배열에 마커 추가
                markers.push(marker);
            });
        })
        .catch(error => console.error('주소를 이용하여 좌표 정보를 가져오는 중 오류 발생:', error));
});

// 지도에 표시된 마커들을 모두 제거하는 함수
function removeMarkers() {
    markers.forEach(marker => {
        marker.setMap(null); // 마커를 지도에서 제거
    });
    markers = []; // markers 배열 초기화
}
