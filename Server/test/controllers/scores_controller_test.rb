require 'test_helper'

class ScoresControllerTest < ActionController::TestCase
  setup do
    @score = scores(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:scores)
  end

  test "should create score" do
    assert_difference('Score.count') do
      post :create, score: { answer_id: @score.answer_id, target_id: @score.target_id }
    end

    assert_response 201
  end

  test "should show score" do
    get :show, id: @score
    assert_response :success
  end

  test "should update score" do
    put :update, id: @score, score: { answer_id: @score.answer_id, target_id: @score.target_id }
    assert_response 204
  end

  test "should destroy score" do
    assert_difference('Score.count', -1) do
      delete :destroy, id: @score
    end

    assert_response 204
  end
end
