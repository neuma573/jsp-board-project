// url query string을 object로 변환
function query2object (qstr) {
  return JSON.parse('{"' + qstr.replace(/&amp;/g, '","').replace(/&/g, '","').replace(/=/g, '":"') + '"}', function (key, value) {
    return key === '' ? value : decodeURIComponent(value)
  })
}

const form2obj = function (form) {
  var queryStr = serializeForm(form);
  return query2object(queryStr);
};

/*!
 * Serialize all form data into a query string
 * (c) 2018 Chris Ferdinandi, MIT License, https://gomakethings.com
 * @param  {Node}   form The form to serialize
 * @return {String}      The serialized form data
 * 예) serializeForm(document.getElementById('frm'))
 */
const serializeForm = function (form) {
  // Setup our serialized data
  var serialized = [];

  // Loop through each field in the form
  for (var i = 0; i < form.elements.length; i++) {
    var field = form.elements[i];

    // Don't serialize fields without a name, submits, buttons, file and reset inputs, and disabled fields
    if (!field.name || field.disabled || field.type === 'file' || field.type === 'reset' || field.type === 'submit' || field.type === 'button') continue;

    if (field.type === 'select-multiple') {
      // If a multi-select, get all selections
      for (var n = 0; n < field.options.length; n++) {
        if (!field.options[n].selected) continue;
        serialized.push(encodeURIComponent(field.name) + '=' + encodeURIComponent(field.options[n].value));
      }
    } else if ((field.type !== 'checkbox' && field.type !== 'radio') || field.checked) {
      // Convert field data to a query string
      serialized.push(encodeURIComponent(field.name) + '=' + encodeURIComponent(field.value));
    }
  }

  return serialized.join('&');
};

function deepCopy (obj) {
  if (typeof obj !== 'object' || obj === null) {
    return obj
  }

  const clone = Array.isArray(obj) ? [] : {}
  for (var key in obj) {
    clone[key] = deepCopy(obj[key])
  }
  return clone
}

function serializable (o) {
  for (const k in o) {
    if (typeof (o[k]) == 'function') {
      delete o[k]
    } else if (o[k] instanceof Array) {
      serializable(o[k])
    } else if (o[k] !== null && typeof (o[k]) == 'object') {
      serializable(o[k])
    }
  }
}

/**
 * electron에서 object를 send() 할 때 function이 포함되 있으면 exception이 발생한다.
 * https://www.electronjs.org/docs/api/ipc-renderer#ipcrenderersendchannel-args
 * 이를 막기위해 전송 불가능한 속성은 삭제한다.
 */
function toSerializable (obj) {
  const clone = deepCopy(obj)
  serializable(clone)
  return clone
}

/**
 * left padding 문자열 반환
 * ex. pad('123', 5) --> '00123'
 */
function pad (n, w, z) {
  z = z || '0'
  n = n + ''
  return n.length >= w ? n : new Array(w - n.length + 1).join(z) + n
}

const array = {
  // 배열 요소 삭제
  remove (ary, idx) {
    return ary.splice(idx, 1)[0]
  },
  // 요소 insert
  insert (ary, idx, item) {
    ary.splice(idx, 0, item)
  },
  // 모든 match 인덱스
  indexFilter (ary, fn) {
    return ary.map((d, i) => fn(d) ? i : undefined).filter(d => d)
  },
  // 마지막 match 인덱스
  findLastIndex (ary, fn) {
    return (ary
      .map((val, i) => [i, val])
      .filter(([i, val]) => fn(val, i, ary))
      .pop() || [-1])[0]
  }
}

const date = {
  // date.format(new Date(), 'yyyy-MM-dd hh:mm:ss') -> 2020-01-02 13:34:25
  format (dt, fmt) {
    var z = {
      M: dt.getMonth() + 1,
      d: dt.getDate(),
      h: dt.getHours(),
      m: dt.getMinutes(),
      s: dt.getSeconds()
    };
    fmt = fmt || 'yyyy-MM-dd hh:mm:ss'
    fmt = fmt.replace(/(M+|d+|h+|m+|s+)/g, function (v) {
      return ((v.length > 1 ? '0' : '') + z[v.slice(-1)]).slice(-2)
    })

    return fmt.replace(/(y+)/g, function (v) {
      return dt.getFullYear().toString().slice(-v.length)
    })
  },
  toDate (timestamp) {
    return new Date(parseInt(timestamp))
  },
  flat (dt) {
    return this.format(dt, 'yyyyMMddhhmmss')
  },
  flatDate (dt) {
    return this.format(dt, 'yyyyMMdd')
  },
  flatTime (dt) {
    return this.format(dt, 'hhmmss')
  }
}

const html = {
  escape (html) {
    return html.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&apos;').replace(/ /g, '&nbsp;')
  },
  unescape (text) {
    return text.replace(/&amp;/g, '&').replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&quot;/g, '"').replace(/&apos;/g, "'").replace(/&nbsp;/g, ' ')
  },
  replaceWhiteSpace (html) {
    return html.replace(/\n/g, '<br>').replace(/\t/g, '    ').replace(/ /g, '&nbsp;')
  },
  innerText (html) {
    var div = document.createElement('div');
    div.innerHTML = html.replace('<br>', '\n')
    return div.innerText
  }
}

const sys = {
  basename (path) {
    return path.split(/[\\/]/).pop()
  }
}

function sleep (msec) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve(true)
    }, msec)
  })
}

/**
 * 문자열 포맷팅
 * example : format('som{0} t{0}x{1}', 'E','T'); --> 'somE tExT'
 */
function format (fmt, args) {
	var	ptn	= /\{\d+\}/g;
	var	args = Array.prototype.slice.call(arguments);
	var	fmt	= args.shift();
	return fmt.replace(ptn,	function(capture){ return args[capture.match(/\d+/)]; });
};
