function editInit(contentId) {
	let editor = tinymce.init({
	    selector: '#'+contentId,
	    height: 500,
	    menubar: false,
	    plugins: 'image code table lists link emoticons media',
	    toolbar: 'undo redo | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist | table link media blockquote | fontfamily fontsize forecolor backcolor | emoticons | link image | code', // 툴바 버튼들 추가
	    automatic_uploads: false,
	    file_picker_types: 'image',

	    // 이미지 업로드 버튼 클릭 시 파일 선택 창 열기
	    file_picker_callback: function (callback, value, meta) {
	        let input = $('<input>').attr({
	            type: 'file',
	            accept: 'image/*'
	        });

	        input.on('change', function () {
	            let file = this.files[0];
	            let formData = new FormData();
	            formData.append('files', file);
	            formData.append('boardId', 0);  
	            formData.append('userId', "${sessionScope.user.userId}");   
	            formData.append('basePath', "commonImg");
	            
	            // AJAX로 이미지 업로드
	            $.ajax({
	                url: '/file/imgUpload.do',
	                type: 'POST',
	                data: formData,
	                contentType: false,
	                processData: false,
	                dataType: 'json',
	                success: function (data) {
	                    if (data.success) {
	                        callback(data.url, { title: file.name });
	                    } else {
	                        alert(data.message);
	                    }
	                },
	                error: function () {
	                    alert('서버 오류로 인해 업로드에 실패했습니다.');
	                }
	            });
	        });

	        input.click();
	    },
	 	// 링크 삽입 시 자동으로 YouTube URL을 처리하여 비디오로 변환
	    link_class_list: [
	        { title: 'None', value: '' },
	        { title: 'External', value: 'external' }
	    ],
	    // 미디어 자동 임베드
	    media_embed: true,
	    media_url_validators: [function(url) {
	        // YouTube URL 형식을 인식하여 iframe으로 변환 (추가 파라미터도 허용)
	        return /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:[^\/\n]+\/\S+|(?:v|e(?:mbed)?)\/([a-zA-Z0-9_-]+))|youtu\.be\/([a-zA-Z0-9_-]+))(\?[\S]*)?/.test(url);
	    }],
	});
	return editor;
}

function editEnd(editor) {
	editor.then(function(instances) {
	  instances[0].destroy();
	});
}
