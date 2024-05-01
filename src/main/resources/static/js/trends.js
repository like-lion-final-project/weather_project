$(document).ready(function() {
    var trendsDataString = $('#trendsData').attr('data-trends');
    var feedbackDataString = $('#feedbackData').attr('data-feedback');

    // 데이터가 있는지 확인하기 위해 콘솔 로그로 출력
    console.log('trendsData:', trendsDataString);
    console.log('feedbackData:', feedbackDataString);

    try {
        var trendsData = JSON.parse(trendsDataString);
        var feedbackCountsByCategory = JSON.parse(feedbackDataString);

        // 데이터가 비어있을 때도 그래프 생성
        createRealtimeTrendsChart(trendsData);
        createFeedbackCountsChart(feedbackCountsByCategory);
    } catch (error) {
        console.error('데이터 처리 오류:', error);
    }

    function createRealtimeTrendsChart(data) {
        var ctx = document.getElementById('realtime-trends-chart').getContext('2d');
        var labels = data.map(function(trend) { return trend.category; });
        var dataValues = data.map(function(trend) { return trend.averageRating; });

        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '별점 그래프',
                    data: dataValues,
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    function createFeedbackCountsChart(data) {
        var ctx = document.getElementById('feedback-counts-chart').getContext('2d');
        var labels = data.map(function(entry) { return entry.category; });
        var dataValues = data.map(function(entry) { return entry.feedbackCount; });

        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '피드백 그래프',
                    data: dataValues,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
});