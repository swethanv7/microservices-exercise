import { useNavigate } from "react-router-dom";

function HomePage() {
  const navigate = useNavigate();

  return (
    <div style={{ padding: "20px" }}>
      <h1>Home Page</h1>
      <button onClick={() => navigate("/products")}>
        Go To Products Page
      </button>
    </div>
  );
}

export default HomePage;