// 공통 유효성 검사 함수
var validationUtil = {
    // 입력값이 비어있는지 확인
    isEmpty: function (value) {
        return !(value.trim() === "");
    },

    // 최소 길이 체크
    minLength: function (value, length) {
        return value.length >= length;
    },

    // 최대 길이 체크
    maxLength: function (value, length) {
        return value.length <= length;
    },

    // 숫자만 포함하는지 체크
    isNumeric: function (value) {
        return /^[0-9]+$/.test(value);
    },

    // 이메일 형식 체크
    isEmail: function (value) {
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(value);
    },

    // 비밀번호 강도 체크 (영문 + 숫자 + 특수문자 포함, 최소 8자 이상)
    isStrongPassword: function (value) {
        var passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;
        return passwordPattern.test(value);
    },

    // 전화번호 형식 체크 (000-0000-0000 또는 000-000-0000)
    isPhoneNumber: function (value) {
        var phonePattern = /^(\d{2,3})-(\d{3,4})-(\d{4})$/;
        return phonePattern.test(value);
    },

    // 입력 필드의 유효성을 검사하고 메시지를 표시하는 함수
    validateField: function (selector, validator, message) {
        var value = $(selector).val();
        if (!validator(value)) {
            alert(message);
            $(selector).focus();
            return false;
        }
        return true;
    }
};

// 공통 AJAX 요청 함수
function ajaxRequest(url, data, successCallback, loginCheck) {
	console.log("data",data);
    $.ajax({
        url: url,
        type: "POST",
        data:  JSON.stringify(data),
        dataType: "json",
		xhrFields: {
	        withCredentials: true // 쿠키를 포함한 요청 보내기
	    },
		contentType: 'application/json',
        success: function (response) {
			console.log(response);
            if (response.success) {
                if (successCallback) successCallback(response);
            } else {
                handleAjaxError(loginCheck, response); // 공통 에러 처리 함수 호출
            }
        },
        error: function (xhr, status, error) {
            handleAjaxError(loginCheck, null, xhr, status, error); // 공통 에러 처리 함수 호출
        }
    });
}

// 공통 AJAX 요청 함수
function ajaxRequest(url, data, successCallback) {
	console.log("data",data);
    $.ajax({
        url: url,
        type: "POST",
        data:  JSON.stringify(data),
        dataType: "json",
		xhrFields: {
	        withCredentials: true // 쿠키를 포함한 요청 보내기
	    },
		contentType: 'application/json',
        success: function (response) {
			console.log(response);
            if (response.success) {
                if (successCallback) successCallback(response);
            } else {
                handleAjaxError(false,response); // 공통 에러 처리 함수 호출
            }
        },
        error: function (xhr, status, error) {
            handleAjaxError(false, null, xhr, status, error); // 공통 에러 처리 함수 호출
        }
    });
}

// 공통 AJAX 요청 함수
function ajaxRequestFile(url, data, successCallback) {
    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json",
		xhrFields: {
	        withCredentials: true // 쿠키를 포함한 요청 보내기
	    },
		processData: false, // FormData 사용 시 false
		contentType: false, // 파일 업로드 시 false
        success: function (response) {
			console.log("success",response);
            if (response.success) {
                if (successCallback) successCallback(response);
            } else {
                handleAjaxError(response); // 공통 에러 처리 함수 호출
            }
        },
        error: function (xhr, status, error) {
            handleAjaxError(null, xhr, status, error); // 공통 에러 처리 함수 호출
        }
    });
}


// 공통 에러 처리 함수
function handleAjaxError(loginCheck, response, xhr, status, error, ) {
	let responseData = response || xhr.responseJSON;
	console.log("error", responseData);

    if(!loginCheck && xhr.status == 401) {
		alert("로그인이 필요한 화면 입니다. 로그인 페이지로 이동합니다.");
		window.location.href ='/user/login.do'; 
	} else if (responseData) {
        alert(responseData.message || "알 수 없는 오류");
    } else {
        alert("서버 통신 중 오류 발생! \n" +
              "상태 코드: " + xhr.status + "\n" +
              "에러 메시지: " + (error || "알 수 없는 오류"));
    }
}


function setupFileUpload(options) {

	let dropZone = $(options.dropZone || '#dropZone');
	let fileInput = $(options.fileInput || '#files');
	let fileList = $(options.newFileList || '#fileList');
	
    let uploadedFiles = [];
	
    let maxFileSize = options.maxFileSize || 10 * 1024 * 1024; // 기본 10MB 제한
    // 드래그 앤 드롭 이벤트 핸들러
    dropZone.on('dragover', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.addClass('dragover');
    });
    dropZone.on('dragleave', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.removeClass('dragover');
    });
    dropZone.on('drop', function(e) {
        e.preventDefault();
        dropZone.removeClass('dragover');

        let files = e.originalEvent.dataTransfer.files;
        handleFiles(files);
    });
    // 클릭 시 파일 선택 창 열기
    dropZone.on('click', function() {
		
        fileInput.click();
    });
    // 파일 선택 시 처리
    fileInput.on('change', function() {
        handleFiles(this.files);
        fileInput.val(''); // 파일 목록 초기화
    });
    // 파일 리스트를 추가하는 함수
    function handleFiles(files) {
        for (let i = 0; i < files.length; i++) {
            let file = files[i];

            // 파일 크기 제한 체크
            if (file.size > maxFileSize) {
                alert("파일 크기는 10MB를 초과할 수 없습니다: " + file.name);
                continue;
            }

			// 중복 체크 (람다식 대신 for문 사용)
	        let isDuplicate = false;
	        for (let j = 0; j < uploadedFiles.length; j++) {
	            if (uploadedFiles[j].name === file.name) {
	                isDuplicate = true;
	                break;
	            }
	        }

	        if (isDuplicate) {
	            alert("같은 파일은 여러 번 추가할 수 없습니다: " + file.name);
	            continue;
	        }
			
            // 배열에 추가
            uploadedFiles.push(file);

            // UI에 추가
			fileList.append('<li>' + files[i].name + 
			                    ' <button name="removeBtn" data-file="'+ files[i].name +'">제거</button></li>'); // 화면에 파일명과 제거 버튼 추가
        }
    }
    // 파일 제거 버튼 클릭 시 처리
    fileList.on('click', '[name="removeBtn"]', function() {
		let fileName = $(this).data('file');
		 
	    // uploadedFiles 배열에서 해당 파일을 제거
	    uploadedFiles = uploadedFiles.filter(function(file) {
	        return file.name !== fileName;
	    });
	
	    // 해당 파일을 목록에서 제거
	    $(this).parent().remove();
    });

    return uploadedFiles ; // 업로드할 파일 목록 가져오기
   
}

function setupFileUploadUpdate(options) {
    // 기본 설정값
    let dropZone = $(options.dropZone || '#dropZone');
    let fileInput = $(options.fileInput || '#files');
    let newFileList = $(options.newFileList || '#newFileList');
    let existingFileList = $(options.existingFileList || '#existingFileList');
    let remainingFileIds = $(options.remainingFileIds || '#remainingFileIds');
    let maxFileSize = options.maxFileSize || 10 * 1024 * 1024; // 기본 10MB 제한

    let uploadedFiles = []; // 새로 업로드된 파일 목록
    let existingFiles = []; // 기존 파일 목록
	
	
    // 드래그 앤 드롭 이벤트 핸들러
    dropZone.on('dragover', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.addClass('dragover');
    });

    dropZone.on('dragleave', function(e) {
        e.preventDefault();
        e.stopPropagation();
        dropZone.removeClass('dragover');
    });

    dropZone.on('drop', function(e) {
        e.preventDefault();
        dropZone.removeClass('dragover');
        handleNewFiles(e.originalEvent.dataTransfer.files);
    });

    dropZone.on('click', function() {
        fileInput.click();
    });

    fileInput.on('change', function() {
        handleNewFiles(this.files);
        fileInput.val('');
    });

    function handleNewFiles(files) {
        for (let i = 0; i < files.length; i++) {
            let file = files[i];

            if (file.size > maxFileSize) {
                alert("파일 크기는 10MB를 초과할 수 없습니다: " + file.name);
                continue;
            }

            if (uploadedFiles.some(f => f.name === file.name)) {
                alert("같은 파일은 여러 번 추가할 수 없습니다: " + file.name);
                continue;
            }

            uploadedFiles.push(file);
			
			// UI에 추가
			newFileList.append('<li>' + files[i].name + 
			                    ' <button name="newFileRemoveBtn" data-file="'+ files[i].name +'">제거</button></li>'); // 화면에 파일명과 제거 버튼 추가
        }
    }

    newFileList.on('click', '[name="newFileRemoveBtn"]', function() {
        let fileName = $(this).data('file');
		// uploadedFiles 배열에서 해당 파일을 제거
		uploadedFiles = uploadedFiles.filter(function(file) {
		    return file.name !== fileName;
		});
         $(this).parent().remove();
    });

    existingFileList.on('click', '[name="removeBtn"]', function() {
        let fileId = $(this).data('file'); // 기존 파일 ID
		// existingFiles 배열에서 해당 파일 ID를 제거
		existingFiles = existingFiles.filter(function(id) {
		    return id !== fileId;
		});
        $(this).parent().remove();
        updateRemainingFileIds();
    });

    function updateRemainingFileIds() {
        remainingFileIds.val(existingFiles.join(','));
    }
	
	// 새로 업로드된 파일 목록 가져오기
	function getUploadedFiles() {
	    return uploadedFiles;
	}
	
	function existingFileListFind() {
		// 기존 파일 목록을 읽어 existingFiles 배열에 추가
	    existingFileList.find('button[name="removeBtn"]').each(function() {
			
			let fileId = $(this).data('file');
	        existingFiles.push(fileId);
	    });
	}
	
	return {
	       getUploadedFiles: getUploadedFiles,
	       updateRemainingFileIds: updateRemainingFileIds,
		   existingFileListFind:existingFileListFind
	};
}





