require 'test_helper'

class TargetsControllerTest < ActionController::TestCase
  setup do
    @target = targets(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:targets)
  end

  test "should create target" do
    assert_difference('Target.count') do
      post :create, target: { answer: @target.answer, fact: @target.fact, latitude: @target.latitude, longitude: @target.longitude, picture: @target.picture, question: @target.question }
    end

    assert_response 201
  end

  test "should show target" do
    get :show, id: @target
    assert_response :success
  end

  test "should update target" do
    put :update, id: @target, target: { answer: @target.answer, fact: @target.fact, latitude: @target.latitude, longitude: @target.longitude, picture: @target.picture, question: @target.question }
    assert_response 204
  end

  test "should destroy target" do
    assert_difference('Target.count', -1) do
      delete :destroy, id: @target
    end

    assert_response 204
  end
end
