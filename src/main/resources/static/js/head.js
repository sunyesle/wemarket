axios.interceptors.request.use(
  (config) => {
    config.url = contextPath + config.url;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);


axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response.data.msg) {
      alert(error.response.data.msg);
    } else {
      alert(error);
    }
    return Promise.reject(error);
  }
);