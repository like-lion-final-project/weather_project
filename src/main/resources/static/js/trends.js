$(document).ready(function() {
    // HTML 속성에서 Thymeleaf 데이터 가져오기
    var trendsDataString = $('#trendsData').attr('data-trends');
    var feedbackDataString = $('#feedbackData').attr('data-feedback');

    // JSON 문자열을 JavaScript 객체로 변환
    var trendsData = JSON.parse(trendsDataString);
    var feedbackCountsByCategory = JSON.parse(feedbackDataString);
    // 실시간 트렌드 그래프 생성
    function createRealtimeTrendsChart() {
        var ctx = document.getElementById('realtime-trends-chart').getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: trendsData.map(function(trend) { return trend.category }), // 트렌드 카테고리
                datasets: [{
                    label: '별점 그래프',
                    data: trendsData.map(function(trend) { return trend.averageRating}), // 트렌드 평균 별점
                    backgroundColor: 'rgba(255, 99, 132, 0.2)', // 바 색상
                    borderColor: 'rgba(255, 99, 132, 1)', // 바 테두리 색상
                    borderWidth: 1
                }]
            },
            options: {
                responsive: false, // 자동 크기조절 비활성화
                scales: {
                    y: {
                        beginAtZero: true // y축이 0부터 시작하도록 설정
                    }
                }
            }
        });
    }

    // 카테고리별 피드백 수 그래프 생성
    function createFeedbackCountsChart() {
        var ctx = document.getElementById('feedback-counts-chart').getContext('2d');
        var labels = [];
        var data = [];

        feedbackCountsByCategory.forEach(function(entry) {
            labels.push(entry.category);
            data.push(entry.feedbackCount);
        });

        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels, // 카테고리
                datasets: [{
                    label: '피드백 그래프',
                    data: data, // 피드백 수
                    backgroundColor: 'rgba(54, 162, 235, 0.2)', // 바 색상
                    borderColor: 'rgba(54, 162, 235, 1)', // 바 테두리 색상
                    borderWidth: 1
                }]
            },
            options: {
                responsive: false, // 자동 크기조절 비활성화
                scales: {
                    y: {
                        beginAtZero: true // y축이 0부터 시작하도록 설정
                    }
                }
            }
        });
    }

    // 페이지 로드 시 초기 실행
    createRealtimeTrendsChart();
    createFeedbackCountsChart();
});