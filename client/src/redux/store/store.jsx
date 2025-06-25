import { configureStore } from "@reduxjs/toolkit";
import authSlice from "../slices/authSlice.jsx";

 const store = configureStore({
  reducer: {
    user: authSlice,
  },
});

export default store;

