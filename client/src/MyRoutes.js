import { Route, Routes } from "react-router-dom";
import Home from "./Pages/Home";
import Ddont from "./Pages/Ddont";

export default function MyRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/" element={<Ddont />} />
    </Routes>
  );
}
