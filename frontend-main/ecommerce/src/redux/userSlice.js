import { createSlice } from "@reduxjs/toolkit";

// Define the initial state with nested 'login' object
const initialState = {
  login: {
    id: null,
    name: null,
    email: null,
    isLoggedIn: false,
    role: null,
  },
};

// Create the login slice
const loginSlice = createSlice({
  name: "login",
  initialState,
  reducers: {
    login: (state, action) => {
      state.id = action.payload.id;
      state.name = action.payload.name;
      state.email = action.payload.email;
      state.role = action.payload.role;
      state.token = action.payload.token || localStorage.getItem("token");
      state.isLoggedIn = true;

      //console.log(state);
    },
    logout: (state) => {
      state.id = null;
      state.name = null;
      state.email = null;
      state.isLoggedIn = false;
      state.token = null;
      state.role = null;
    },
  },
});

// Export actions for dispatching
export const { login, logout } = loginSlice.actions;

// Export the reducer to be used in the store
export default loginSlice.reducer;
